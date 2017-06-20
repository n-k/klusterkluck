package com.github.nk.klusterfuck.admin.model;

import javax.persistence.*;

/**
 * Created by nk on 20/6/17.
 */
@Entity
@Table(name = "user_namespaces")
public class UserNamespace {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private String displayName;
	private String gitUser;
	private String gitPassword;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getGitUser() {
		return gitUser;
	}

	public void setGitUser(String gitUser) {
		this.gitUser = gitUser;
	}

	public String getGitPassword() {
		return gitPassword;
	}

	public void setGitPassword(String gitPassword) {
		this.gitPassword = gitPassword;
	}
}
