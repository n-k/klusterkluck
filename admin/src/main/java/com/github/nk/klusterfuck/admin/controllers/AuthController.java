package com.github.nk.klusterfuck.admin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by nipunkumar on 18/06/17.
 */
@Api("Auth")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;
	@Value("${keycloak.realm}")
	private String realm;
	@Value("${keycloak.resource}")
	private String clientId;
	@Value("${app.keycloak.adminUser}")
	private String adminUser;
	@Value("${app.keycloak.adminPassword}")
	private String adminPassword;

	private ObjectMapper mapper = new ObjectMapper();

	@ApiOperation("login")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map login(@RequestBody LoginRequest loginRequest) throws Exception {
		String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
		HttpPost post = new HttpPost(tokenUrl);
		// create payload
		String payload = "client_id=" + clientId + "&grant_type=password&username=" + loginRequest.getUsername()
				+ "&password=" + loginRequest.getPassword();
		post.setEntity(new StringEntity(payload));
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(post);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode != 200) {
			throw new Exception("Unexpected status code: " + statusCode);
		} else {
			Map map = mapper.readValue(response.getEntity().getContent(), Map.class);
			return map;
		}
	}

	@ApiOperation("register")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestBody RegisterRequest registerRequest) throws Exception {
		Keycloak client = getKeycloakclient();
		try {

			UserRepresentation user = new UserRepresentation();
			user.setUsername(registerRequest.getEmail());
			user.setEmail(registerRequest.getEmail());
			user.setFirstName(registerRequest.getFirstName());
			user.setLastName(registerRequest.getLastName());
			user.setEnabled(true);
			user.setRealmRoles(Arrays.asList("user"));
			Response response = client.realm(realm).users()
					.create(user);
			System.out.println("Repsonse: " + response.getStatusInfo());
			System.out.println(response.getLocation());
			String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
			System.out.println(userId);

			RoleRepresentation userRole =
					client.realm(realm).roles().get("user").toRepresentation();
			client.realm(realm).users().get(userId).roles()
					.realmLevel()
					.add(Arrays.asList(userRole));

			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setType(CredentialRepresentation.PASSWORD);
			credential.setValue(registerRequest.getPassword());
			credential.setTemporary(false);
			client.realm(realm).users().get(userId).
					resetPassword(credential);

			return "{\"status\": \"OK\"}";
		} finally {
			client.close();
		}
	}

	@RequestMapping(value = "/whoami", method = RequestMethod.GET)
	public String whoami(Principal principal) {
		return principal.getName();
	}

	@ApiOperation("logout")
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(Principal principal) throws Exception {
//        if (principal instanceof KeycloakAuthenticationToken) {
//            KeycloakAuthenticationToken kat = (KeycloakAuthenticationToken) principal;
//            KeycloakSecurityContext ksc = kat.getAccount().getKeycloakSecurityContext();
//            String bearerToken = ksc.getIdTokenString();
//
//            String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
//            HttpPost post = new HttpPost(tokenUrl);
//            post.setHeader("Authorization", "Bearer " + bearerToken);
//            HttpClient client = HttpClientBuilder.create().build();
//            HttpResponse response = client.execute(post);
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();
//            if (statusCode != 200) {
//                throw new Exception("Unexpected status code: " + statusCode);
//            }
//        }
		return "OK";
	}

	private Keycloak getKeycloakclient() {
		return Keycloak.getInstance(
				authServerUrl,
				realm,
				adminUser,
				adminPassword,
				clientId);
	}
}