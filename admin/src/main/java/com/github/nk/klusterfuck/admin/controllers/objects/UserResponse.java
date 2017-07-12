package com.github.nk.klusterfuck.admin.controllers.objects;

import com.github.nk.klusterfuck.admin.model.User;

/**
 * Created by nk on 22/6/17.
 */
public class UserResponse {
	private User user;
	private String accessToken;

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

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}
}
