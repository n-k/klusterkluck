package com.github.nk.klusterfuck.admin.model;

import javax.persistence.*;

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
}
