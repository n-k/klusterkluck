package com.github.nk.klusterfuck.admin.controllers.objects;

/**
 * Created by nk on 20/6/17.
 */
public class LoginRequest {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
