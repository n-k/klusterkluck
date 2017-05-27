package com.github.nk.klusterfuck.services;

import com.github.nk.klusterfuck.model.Function;
import de.ayesolutions.gogs.client.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public List<Function> list() {
        return em.createQuery("select f from Function f")
                .getResultList();
    }

    public Function create(String name) throws RepoCreationException {
        Repository repo = gogsService.createRepo(name);
        Function fn = new Function();
        fn.setName(name);
        fn.setGitUrl(repo.getCloneUrl());
        em.persist(fn);
        return fn;
    }
}
