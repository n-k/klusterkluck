package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.controllers.CreateFunctionRequest;
import com.github.nk.klusterfuck.admin.model.KFFunction;
import com.github.nk.klusterfuck.admin.model.User;
import com.github.nk.klusterfuck.admin.model.UserNamespace;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by nipunkumar on 27/05/17.
 */
@Service
@Transactional
public class FunctionsService {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private GogsService gogsService;
	@Autowired
	private KubeService kubeService;
	@Autowired
	private IdService idService;
	@Autowired
	UsersService usersService;

	private UserNamespace getDefaultNamespace() {
		User currentUser = usersService.getCurrentUser();
		return currentUser.getNamespaces().get(0);
	}

	public List<KFFunction> list() {
		TypedQuery<KFFunction> query =
				em.createQuery("select f from KFFunction f where f.owner = :owner", KFFunction.class);
		query.setParameter("owner", getDefaultNamespace());
		return query.getResultList();
	}

	public KFFunction create(CreateFunctionRequest cfr) throws Exception {
		UserNamespace userNamespace = getDefaultNamespace();
		String name = cfr.getName();
		RepoInitializer initializer = RepoTemplates.getFunctionInitializer(cfr.getType());
		RepoInfo repo = gogsService.createRepo(userNamespace, name, initializer);
		KFFunction fn = new KFFunction();
		fn.setName(name);
		fn.setGitUrl(repo.getGitUrl());
		fn.setCommitId(repo.getCommitId());

		String cloneUrl = "http://" + userNamespace.getGitUser()
				+ ":" + userNamespace.getGitPassword() + "@gogs." + userNamespace.getName()
				+ ".svc.cluster.local/" + userNamespace.getGitUser() + "/" + cfr.getName() + ".git";
		kubeService.cloneInCloud9Pod(userNamespace.getName(), cloneUrl);

		KubeDeployment fnService =
				kubeService.createFnService(
						userNamespace.getName(),
						idService.newId(),
						cfr.getType(),
//						repo.getGitUrl(),
						cloneUrl, // use internal URL with auth, pod may not be able to resolve ingress url
						userNamespace.getGitUser(),
						userNamespace.getGitPassword(),
						repo.getCommitId());
		fn.setNamespace(fnService.getNamespace());
		fn.setDeployment(fnService.getDeployment());
		fn.setService(fnService.getService());
		fn.setOwner(getDefaultNamespace());
		String ingressUrl = userNamespace.getName() + "." + kubeService.getDomain() + "/" + fn.getService();
		fn.setIngressUrl(ingressUrl);
		em.persist(fn);
		return fn;
	}

	public KFFunction get(String fnId) {
		TypedQuery<KFFunction> query
				= em.createQuery("select f from KFFunction f where f.id = :id", KFFunction.class);
		query.setParameter("id", Long.parseLong(fnId));
		return query.getSingleResult();
	}

	/**
	 * Get list of commits for default branch
	 *
	 * @param id
	 */
	public List<Version> getCommits(String id) throws Exception {
		UserNamespace userNamespace = getDefaultNamespace();
		KFFunction function = get(id);
		Path fnTmp = null;
		try {
			CredentialsProvider credentialsProvider
					= new UsernamePasswordCredentialsProvider(
						userNamespace.getGitUser(),
						userNamespace.getGitPassword());
			fnTmp = Files.createTempDirectory("fn_tmp");
			try (Git git = Git.cloneRepository()
					.setURI(function.getGitUrl())
					.setCredentialsProvider(credentialsProvider)
					.setDirectory(fnTmp.toFile().getCanonicalFile())
					.call()) {
				org.eclipse.jgit.lib.Repository repo = git.getRepository();
				RevWalk walk = new RevWalk(repo);

				List<Version> branchCommits = new ArrayList<>();

				List<Ref> branches = git.branchList().call();
				for (Ref branch : branches) {
					String branchName = branch.getName();
					Iterable<RevCommit> commits = git.log().all().call();
					for (RevCommit commit : commits) {
						boolean foundInThisBranch = false;
						RevCommit targetCommit = walk.parseCommit(repo.resolve(
								commit.getName()));
						for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
							if (e.getKey().startsWith(Constants.R_HEADS)) {
								if (walk.isMergedInto(targetCommit, walk.parseCommit(
										e.getValue().getObjectId()))) {
									String foundInBranch = e.getValue().getName();
									if (branchName.equals(foundInBranch)) {
										foundInThisBranch = true;
										break;
									}
								}
							}
						}

						if (foundInThisBranch) {
							Version version = new Version();
							version.setId(commit.getName());
							version.setAuthor(commit.getAuthorIdent().getName());
							version.setTimestamp(new Date(commit.getCommitTime()));
							version.setMessage(commit.getFullMessage());
							branchCommits.add(version);
						}
					}
				}
				return branchCommits;
			}
		} catch (Exception e) {
			// delete gogs repo
			throw new Exception("Could not create repo", e);
		} finally {
			if (fnTmp != null) {
				try {
					FileUtils.deleteDirectory(fnTmp.toFile());
				} catch (IOException e) {
					throw new Exception("Could not create repo", e);
				}
			}
		}
	}

	public void setVersion(String id, String versionId) {
		KFFunction function = get(id);
		UserNamespace userNamespace = getDefaultNamespace();
		kubeService.updateFnDeployment(userNamespace.getName(), function.getDeployment(), versionId);
		function.setCommitId(versionId);
		em.persist(function);
	}

	public void delete(String id) throws Exception {
		KFFunction function = get(id);
		kubeService.deleteService(function.getNamespace(), function.getService());
		kubeService.deleteDeployment(function.getNamespace(), function.getDeployment());
		gogsService.deleteRepo(getDefaultNamespace(), function.getName());
		em.remove(function);
	}
}
