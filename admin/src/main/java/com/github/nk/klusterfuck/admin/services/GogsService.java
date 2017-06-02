package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.KubeConfigType;
import com.github.nk.klusterfuck.admin.model.GogsConnection;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by nipunkumar on 27/05/17.
 */
@Service
@Transactional
public class GogsService {

    private static Logger LOG = LoggerFactory.getLogger(GogsService.class);

    @PersistenceContext
    private EntityManager em;

    @Value("${app.kube.configType:env}")
    private KubeConfigType configType;
    @Value("${GOGS_URL}")
    private String gogsUrl;
    @Value("${GOGS_USER}")
    private String gogsUser;
    @Value("${GOGS_PASSWORD}")
    private String gogsPassword;

    public List<GogsConnection> list() {
        return em.createQuery("select g from GogsConnection g", GogsConnection.class)
                .getResultList();
    }

    public GogsConnection get(String id) {
//        TypedQuery<GogsConnection> query = em.createQuery(
//                "select g from GogsConnection g where g.name = :name",
//                GogsConnection.class);
//        query.setParameter("name", id);
//        return query.getSingleResult();
        GogsConnection connection = new GogsConnection();
        connection.setUrl(gogsUrl);
        connection.setName("default");
        connection.setExternalUrl(gogsUrl);
        connection.setUsername(gogsUser);
        connection.setPassword(gogsPassword);
        return connection;
    }

    public GogsConnection save(GogsConnection g) {
        em.persist(g);
        return g;
    }

    public Repository createRepo(String name, String connection) throws RepoCreationException, MalformedURLException {
        GogsConnection gogs = get(connection);
        String gogsUrl = gogs.getUrl();
        if (configType != KubeConfigType.env && gogs.getExternalUrl() != null && !gogs.getExternalUrl().isEmpty()) {
            gogsUrl = gogs.getExternalUrl();
        }
        String username = gogs.getUsername();
        String password = gogs.getPassword();
        // check gogs URL, only support http(s) for now
        URL url = new URL(gogsUrl);
        String protocol = url.getProtocol();
        if (!("http".equals(protocol) || "https".equals(protocol))) {
            throw new RuntimeException("Currently only HTTP(S) protocol is supported, found: " + protocol);
        }
        GogsClient client = new GogsClient(
                UriBuilder.fromUri(gogsUrl + "/api/v1").build(),
                new AccessToken(null, null, username, password));

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
                    = new UsernamePasswordCredentialsProvider(username, password);
            fnTmp = Files.createTempDirectory("fn_tmp");
            String cloneUrl = repository.getCloneUrl();
            // TODO: ugghhhh, add username, password to URL, don't do this
            cloneUrl = cloneUrl.replaceAll("http://", "http://" + username + ":" + password + "@");
            cloneUrl = cloneUrl.replaceAll("https://", "https://" + username + ":" + password + "@");
            String actualHost = url.getHost();
            if (url.getPort() != -1) {
                actualHost = actualHost + ":" + url.getPort();
            }
            // in case gogs is misconfigured to not report correct clone URL
            cloneUrl = cloneUrl.replaceAll("gogs.localhost", actualHost);
            cloneUrl = cloneUrl.replaceAll("localhost", actualHost);
            repository.setCloneUrl(cloneUrl);
            LOG.warn("Attempting to clone from " + cloneUrl + " to dir " + fnTmp.toString() + " with auth " + username + ":" + password);
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
            repoService.deleteRepository(username, name);
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
