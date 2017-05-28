package com.github.nk.klusterfuck.services;

import com.github.nk.klusterfuck.model.KFFunction;
import de.ayesolutions.gogs.client.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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

    public List<KFFunction> list() {
        return em.createQuery("select f from KFFunction f")
                .getResultList();
    }

    public KFFunction create(String name) throws RepoCreationException {
        Repository repo = gogsService.createRepo(name);
        KFFunction fn = new KFFunction();
        fn.setName(name);
        fn.setGitUrl(repo.getCloneUrl());
        KubeDeployment fnService = kubeService.createFnService(repo.getCloneUrl());
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
}
