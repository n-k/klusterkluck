package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.model.UserNamespace;
import com.github.nk.klusterfuck.admin.tools.gogs.CreateRepositoryRequest;
import com.github.nk.klusterfuck.admin.tools.gogs.Repository;
import com.github.nk.klusterfuck.admin.tools.gogs.SimpleGogsClient;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by nipunkumar on 27/05/17.
 */
@Service
@Transactional
public class GogsService {

	private static Logger LOG = LoggerFactory.getLogger(GogsService.class);

	@Value("${app.domain}")
	private String domain;

	public void deleteRepo(UserNamespace userNamespace, String name) throws Exception {
		String gogsUrl = "http://gogs." + userNamespace.getName() + ".svc.cluster.local";
		String gogsUser = userNamespace.getGitUser();
		String gogsPassword = userNamespace.getGitPassword();
		SimpleGogsClient.deleteRepo(name, gogsUrl, gogsUser, gogsPassword);
	}

	public RepoInfo createRepo(UserNamespace userNamespace, String name, RepoInitializer initer) throws Exception {
		CreateRepositoryRequest cr = new CreateRepositoryRequest();
		cr.setName(name);
		cr.setDescription("...");
		cr.setAutoInit(true);
		cr.setGitIgnores("Eclipse");
		cr.setReadme("Default");
		cr.setLicense("Apache License 2.0");
		cr.setPrivateRepository(true);

		String gogsUrl = "http://gogs." + userNamespace.getName() + "." + domain;
		String gogsUser = userNamespace.getGitUser();
		String gogsPassword = userNamespace.getGitPassword();
		Repository repository =
				SimpleGogsClient.createRepo(cr, gogsUrl, gogsUser, gogsPassword);
		RepoInfo info = new RepoInfo();
		// clone in temp dir and add fn things and push
		Path fnTmp = null;
		try {
			CredentialsProvider credentialsProvider
					= new UsernamePasswordCredentialsProvider(gogsUser, gogsPassword);
			fnTmp = Files.createTempDirectory("fn_tmp");
			repository.setCloneUrl(gogsUrl + "/" + gogsUser + "/" + name + ".git");
			String cloneUrl = repository.getCloneUrl();
			info.setGitUrl(cloneUrl);
			try (Git cloned = Git.cloneRepository()
					.setURI(cloneUrl)
					.setCredentialsProvider(credentialsProvider)
					.setDirectory(fnTmp.toFile().getCanonicalFile())
					.call()) {
				if (initer != null) {
					initer.init(fnTmp.toFile());
				}
				cloned.add()
						.addFilepattern(".")
						.call();

				RevCommit revCommit = cloned.commit()
						.setMessage("setup function files...")
						.call();
				info.setCommitId(revCommit.name());

				cloned.push()
						.setCredentialsProvider(credentialsProvider)
						.call();
			}
			return info;
		} catch (Exception e) {
			// delete gogs repo
			deleteRepo(userNamespace, name);
			throw new RepoCreationException("Could not create repo", e);
		} finally {
			if (fnTmp != null) {
				try {
					FileUtils.deleteDirectory(fnTmp.toFile());
				} catch (IOException e) {
					throw new RepoCreationException("Could not create repo", e);
				}
			}
		}
	}
}
