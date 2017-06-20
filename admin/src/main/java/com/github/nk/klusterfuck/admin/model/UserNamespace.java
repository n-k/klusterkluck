package com.github.nk.klusterfuck.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by nk on 20/6/17.
 */
@Entity
@Table(name = "user_namespaces")
@NamedQueries({
		@NamedQuery(
				name = "UserNamespace.byEmail",
				query = "select un from User u, UserNamespace un where " +
						"u.email = :email and un.owner = u")
})
public class UserNamespace {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@JsonIgnore
	@ManyToOne
	private User owner;

	private String name;
	private String displayName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
