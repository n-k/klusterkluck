package com.github.nk.klusterfuck.services;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by nipunkumar on 27/05/17.
 */
@Service
public class GogsService {

    @Value("${GOGS_URL}")
    private String gogsUrl;
    @Value("${GOGS_USERNAME}")
    private String gogsUsername;
    @Value("${GOGS_PASSWORD}")
    private String gogsPassword;
    @Value("${GOGS_TOKEN}")
    private String gogsToken;
    @Value("${GOGS_TOKEN_NAME}")
    private String gogsTokenName;

    private GogsClient client;

    @PostConstruct
    public void init() throws Exception {
        // check gogs URL, only support http(s) for now
        URL url = new URL(gogsUrl);
        String protocol = url.getProtocol();
        if (!("http".equals(protocol) || "https".equals(protocol))) {
            throw new RuntimeException("Currently only HTTP(S) protocol is supported, found: " + protocol);
        }
        client = new GogsClient(
                UriBuilder.fromUri(gogsUrl + "/api/v1").build(),
                new AccessToken(gogsTokenName, gogsToken));
    }

    public Repository createRepo(String name) throws RepoCreationException {
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
                    = new UsernamePasswordCredentialsProvider(gogsUsername, gogsPassword);
            fnTmp = Files.createTempDirectory("fn_tmp");
            String cloneUrl = repository.getCloneUrl();
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
            // TODO: ugghhhh, add username, password to URL, don't do this
            cloneUrl = cloneUrl.replaceAll("http://", "http://" + gogsUsername + ":" + gogsPassword + "@");
            cloneUrl = cloneUrl.replaceAll("https://", "https://" + gogsUsername + ":" + gogsPassword + "@");
            // in case gogs is misconfigured to not report correct clone URL
            cloneUrl = cloneUrl.replaceAll("localhost", new URL(gogsUrl).getHost());
            repository.setCloneUrl(cloneUrl);
            return repository;
        } catch (Exception e) {
            // delete gogs repo
            repoService.deleteRepository(gogsUsername, name);
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
