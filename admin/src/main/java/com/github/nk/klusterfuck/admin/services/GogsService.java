package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.KubeConfigType;
import de.ayesolutions.gogs.client.GogsClient;
import de.ayesolutions.gogs.client.model.AccessToken;
import de.ayesolutions.gogs.client.model.CreateRepository;
import de.ayesolutions.gogs.client.model.Repository;
import de.ayesolutions.gogs.client.service.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by nipunkumar on 27/05/17.
 */
@Service
@Transactional
public class GogsService {

	private static Logger LOG = LoggerFactory.getLogger(GogsService.class);

	@Value("${app.kube.configType:env}")
	private KubeConfigType configType;
	@Value("${GOGS_URL}")
	private String gogsUrl;
	@Value("${GOGS_USER}")
	private String gogsUser;
	@Value("${GOGS_PASSWORD}")
	private String gogsPassword;

	public String getGogsUrl() {
		return gogsUrl;
	}

	public String getGogsUser() {
		return gogsUser;
	}

	public String getGogsPassword() {
		return gogsPassword;
	}

	public Repository createRepo(String name) throws RepoCreationException, MalformedURLException {
		GogsClient client = new GogsClient(
				UriBuilder.fromUri("http://" + gogsUrl + "/api/v1").build(),
				new AccessToken(null, null, gogsUser, gogsPassword));

		RepositoryService repoService = new RepositoryService(client);
		CreateRepository cr = new CreateRepository();
		cr.setName(name);
		cr.setDescription("...");
		cr.setAutoInit(true);
		cr.setGitIgnores("Eclipse");
		cr.setReadme("Default");
		cr.setLicense("Apache License 2.0");
		cr.setPrivateRepository(true);
		Repository repository = repoService.createRepository(cr);
		// clone in temp dir and add fn things and push
		Path fnTmp = null;
		try {
			CredentialsProvider credentialsProvider
					= new UsernamePasswordCredentialsProvider(gogsUser, gogsPassword);
			fnTmp = Files.createTempDirectory("fn_tmp");
			repository.setCloneUrl("http://" + gogsUrl + "/" + gogsUser + "/" + name + ".git");
			String cloneUrl = repository.getCloneUrl();
			LOG.warn("Attempting to clone from "
					+ cloneUrl + " to dir "
					+ fnTmp.toString() + " with auth " + gogsUser + ":" + gogsPassword);
			try (Git cloned = Git.cloneRepository()
					.setURI(cloneUrl)
					.setCredentialsProvider(credentialsProvider)
					.setDirectory(fnTmp.toFile().getCanonicalFile())
					.call()) {
				File confFile = new File(fnTmp.toFile(), "config.yaml");
				try (FileOutputStream fos = new FileOutputStream(confFile)) {
					IOUtils.copy(new ByteArrayInputStream("command: \"wc\"\n".getBytes()), fos);
				}
				cloned.add()
						.addFilepattern("config.yaml")
						.call();

				cloned.commit()
						.setMessage("setup function files...")
						.call();

				cloned.push()
						.setCredentialsProvider(credentialsProvider)
						.call();
			}
			return repository;
		} catch (Exception e) {
			// delete gogs repo
			repoService.deleteRepository(gogsUser, name);
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
