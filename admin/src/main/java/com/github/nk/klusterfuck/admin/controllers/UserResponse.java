package com.github.nk.klusterfuck.admin.controllers;

import com.github.nk.klusterfuck.admin.model.User;

/**
 * Created by nk on 22/6/17.
 */
public class UserResponse {
	private User user;
	public UserResponse() {}
	public UserResponse(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
