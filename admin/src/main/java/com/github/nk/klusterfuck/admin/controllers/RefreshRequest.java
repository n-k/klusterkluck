package com.github.nk.klusterfuck.admin.controllers;

/**
 * Created by nk on 20/6/17.
 */
public class RefreshRequest {
	private String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
