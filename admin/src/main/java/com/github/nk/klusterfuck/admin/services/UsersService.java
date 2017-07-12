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

	public boolean doesUsernameExist(String username) {
		TypedQuery<User> query = em.createNamedQuery("User.getByUsername", User.class);
		query.setParameter("username", username);
		try {
			return query.getResultList().size() > 0;
		} catch (NoResultException nre) {
			return false;
		}
	}

	public User getCurrentUser() {
		Authentication auth =
				SecurityContextHolder.getContext().getAuthentication();
		return get(auth.getName());
	}

	public User create(String email, String username, String iamId) {
		User u = new User();
		u.setEmail(email);
		u.setUsername(username);
		u.setIamId(iamId);

		UserNamespace namespace = new UserNamespace();
		namespace.setName(idService.newId());
		namespace.setDisplayName(username + "'s namespace");
		namespace.setGitUser(idService.newId());
		namespace.setGitPassword(idService.newId());
		em.persist(namespace);

		u.setNamespaces(Arrays.asList(namespace));
		em.persist(u);

		return u;
	}
}
