package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.model.User;
import com.github.nk.klusterfuck.admin.model.UserNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nk on 20/6/17.
 */
@Service
@Transactional
public class UsersService {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private IdService idService;

	public User get(String email) {
		TypedQuery<User> query = em.createNamedQuery("User.get", User.class);
		query.setParameter("email", email);
		try {
			return query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public User getCurrentUser() {
		Authentication auth =
				SecurityContextHolder.getContext().getAuthentication();
		return get(auth.getName());
	}

	public List<UserNamespace> getNamespaces(String email) {
		TypedQuery<UserNamespace> query = em.createNamedQuery(
				"UserNamespace.byEmail",
				UserNamespace.class);
		query.setParameter("email", email);
		return query.getResultList();
	}

	public User create(String email, String iamId) {
		User u = new User();
		u.setEmail(email);
		u.setIamId(iamId);

		UserNamespace namespace = new UserNamespace();
		namespace.setName(idService.newId());
		namespace.setDisplayName(email + "'s namespace");
		namespace.setGitUser(idService.newId());
		namespace.setGitPassword(idService.newId());
		em.persist(namespace);

		u.setNamespaces(Arrays.asList(namespace));
		em.persist(u);

		return u;
	}
}
