package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.model.User;
import com.github.nk.klusterfuck.admin.model.UserNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by nk on 20/6/17.
 */
@Service
public class UsersService {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private IdService idService;

	public User get(String email) {
		TypedQuery<User> query = em.createNamedQuery("User.id", User.class);
		query.setParameter("email", email);
		return query.getSingleResult();
	}

	public List<UserNamespace> getNamespaces(String email) {
		TypedQuery<UserNamespace> query = em.createNamedQuery(
				"UserNamespace.byEmail",
				UserNamespace.class);
		query.setParameter("email", email);
		return query.getResultList();
	}

	public void create(String email, String iamId) {
		User u = new User();
		u.setEmail(email);
		u.setIamId(iamId);
		em.persist(u);

		UserNamespace namespace = new UserNamespace();
		namespace.setName(idService.newId());
		namespace.setDisplayName(email + "'s namespace");
		namespace.setOwner(u);
		em.persist(namespace);
	}
}
