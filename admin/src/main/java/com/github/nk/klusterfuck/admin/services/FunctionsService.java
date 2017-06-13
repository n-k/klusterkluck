package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.PersistenceUtils;
import com.github.nk.klusterfuck.admin.controllers.CreateFunctionRequest;
import com.github.nk.klusterfuck.admin.model.KFFunction;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

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
public class FunctionsService {
	private GogsService gogsService;
	private KubeService kubeService;
	private IdService idService;

	public FunctionsService(
			GogsService gogsService,
			KubeService kubeService,
			IdService idService) {
		this.gogsService = gogsService;
		this.kubeService = kubeService;
		this.idService = idService;
	}

	public List<KFFunction> list() throws Exception {
		return PersistenceUtils.doInTxn(em -> {
			return em.createQuery("select f from KFFunction f", KFFunction.class)
					.getResultList();
		});
	}

	public KFFunction create(CreateFunctionRequest cfr, RepoInitializer initializer) throws Exception {
		String name = cfr.getName();
		RepoInfo repo = gogsService.createRepo(name, initializer);
		KFFunction fn = new KFFunction();
		fn.setName(name);
		fn.setGitUrl(repo.getGitUrl());
		fn.setCommitId(repo.getCommitId());
		ServiceCreationConfig config = new ServiceCreationConfig();
		config.setName(idService.newId());
		config.setGitUrl(repo.getGitUrl());
		config.setGitUser(gogsService.getGogsUser());
		config.setGitPassword(gogsService.getGogsPassword());
		config.setCommitId(repo.getCommitId());
		config.setIngress(cfr.isIngress());
		config.setHost(cfr.getHost());
		config.setPath(cfr.getPath());
		config.setImage(kubeService.getAgentImage());

		String cloneUrl = "http://" + gogsService.getGogsUser()
				+ ":" + gogsService.getGogsPassword() + "@" + gogsService.getGogsUrl()
				+ "/" + gogsService.getGogsUser() + "/" + cfr.getName() + ".git";
		kubeService.cloneInCloud9Pod(cloneUrl);

		KubeDeployment fnService = kubeService.createFnService(config);
		fn.setNamespace(fnService.getNamespace());
		fn.setDeployment(fnService.getDeployment());
		fn.setService(fnService.getService());
		PersistenceUtils.doInTxn(em -> {
			em.persist(fn);
			return null;
		});
		return fn;
	}

	public KFFunction get(String fnId) throws Exception {
		return PersistenceUtils.doInTxn(em -> {
			TypedQuery<KFFunction> query
					= em.createQuery("select f from KFFunction f where f.id = :id", KFFunction.class);
			query.setParameter("id", Long.parseLong(fnId));
			return query.getSingleResult();
		});
	}

	/**
	 * Get list of commits for default branch
	 *
	 * @param id
	 */
	public List<Version> getCommits(String id) throws Exception {
		KFFunction function = get(id);
		Path fnTmp = null;
		try {
			CredentialsProvider credentialsProvider
					= new UsernamePasswordCredentialsProvider(
					gogsService.getGogsUser(),
					gogsService.getGogsPassword());
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

	public void setVersion(String id, String versionId) throws Exception {
		PersistenceUtils.doInTxn(em -> {
			KFFunction function = null;
			try {
				function = get(id);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			kubeService.updateFnDeployment(function.getDeployment(), versionId);
			function.setCommitId(versionId);
			em.persist(function);
			return null;
		});
	}

	public void delete(String id) throws Exception {
		KFFunction function = get(id);
		kubeService.deleteService(function.getNamespace(), function.getService());
		kubeService.deleteDeployment(function.getNamespace(), function.getDeployment());
		gogsService.deleteRepo(function.getName());
		PersistenceUtils.doInTxn(em -> {
			TypedQuery<KFFunction> query
					= em.createQuery("select f from KFFunction f where f.id = :id", KFFunction.class);
			query.setParameter("id", Long.parseLong(id));
			KFFunction function2 = query.getSingleResult();
			em.remove(function2);
			return null;
		});
	}
}
