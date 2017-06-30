package com.github.nk.klusterfuck.admin.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by nk on 20/6/17.
 */
@Entity
@Table(name = "users")
@NamedQueries({
		@NamedQuery(name = "User.get", query = "select u from User u where u.email = :email")
})
public class User {
	@Id
	private String email;
	private String iamId;
	/*
	Not expecting too many namespaces per user, most of the time it will be 1,
	so, eager fetch is fine for now, else, move this relation to the many-to-one side
	and add a named query to fetch user's namespaces
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<UserNamespace> namespaces;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIamId() {
		return iamId;
	}

	public void setIamId(String iamId) {
		this.iamId = iamId;
	}

	public List<UserNamespace> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(List<UserNamespace> namespaces) {
		this.namespaces = namespaces;
	}
}
