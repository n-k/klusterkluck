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
import org.springframework.stereotype.Service;

import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by nipunkumar on 27/05/17.
 */
@Service
public class GogsService {
//    private GogsClient client = new GogsClient(
//            UriBuilder.fromUri("http://localhost:3000/api/v1").build(),
//            "gogsadmin",
//            "admin",
//            "fdc8c390cec954c609f98ecace6a1bf1f66fe12a");
    private GogsClient client = new GogsClient(
            UriBuilder.fromUri("http://localhost:3000/api/v1").build(),
            new AccessToken("admin", "fdc8c390cec954c609f98ecace6a1bf1f66fe12a"));

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
                    = new UsernamePasswordCredentialsProvider("gogsadmin", "admin");
            fnTmp = Files.createTempDirectory("fn_tmp");
            try (Git cloned = Git.cloneRepository()
                    .setURI(repository.getCloneUrl())
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
            repoService.deleteRepository("gogsadmin", name);
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
