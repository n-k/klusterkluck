package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.model.KFFunction;
import de.ayesolutions.gogs.client.model.Repository;
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

    public List<KFFunction> list() {
        return em.createQuery("select f from KFFunction f", KFFunction.class)
                .getResultList();
    }

    public KFFunction create(String name) throws Exception {
        Repository repo = gogsService.createRepo(name);
        KFFunction fn = new KFFunction();
        fn.setName(name);
        fn.setGitUrl(repo.getCloneUrl());
        KubeDeployment fnService =
                kubeService.createFnService(repo.getCloneUrl(),
                        gogsService.getGogsUser(),
                        gogsService.getGogsPassword(),
                        idService.newId(),
                        "");
        fn.setNamespace(fnService.getNamespace());
        fn.setDeployment(fnService.getDeployment());
        fn.setService(fnService.getService());
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

    public void setVersion(String id, String versionId) {
        KFFunction function = get(id);
        kubeService.createFnService(function.getGitUrl(),
                gogsService.getGogsUser(),
                gogsService.getGogsPassword(),
                function.getService(),
                versionId);
        function.setCommitId(versionId);
        em.persist(function);
    }
}
