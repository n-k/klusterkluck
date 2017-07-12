package com.github.nk.klusterfuck.admin.controllers.objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.keycloak.representations.AccessTokenResponse;

import java.util.Map;

/**
 * Created by nk on 20/6/17.
 */
public class AccesstokenResponseWrapper {
	private AccessTokenResponse atr;

	public AccesstokenResponseWrapper(AccessTokenResponse atr) {
		this.atr = atr;
	}

	public String getToken() {
		return atr.getToken();
	}

	public long getExpiresIn() {
		return atr.getExpiresIn();
	}

	public long getRefreshExpiresIn() {
		return atr.getRefreshExpiresIn();
	}

	public String getRefreshToken() {
		return atr.getRefreshToken();
	}

	public String getTokenType() {
		return atr.getTokenType();
	}

	public String getIdToken() {
		return atr.getIdToken();
	}

	public int getNotBeforePolicy() {
		return atr.getNotBeforePolicy();
	}

	public String getSessionState() {
		return atr.getSessionState();
	}

	@JsonAnyGetter
	public Map<String, Object> getOtherClaims() {
		return atr.getOtherClaims();
	}

}
