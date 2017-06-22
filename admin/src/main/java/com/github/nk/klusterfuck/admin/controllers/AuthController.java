package com.github.nk.klusterfuck.admin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.admin.model.User;
import com.github.nk.klusterfuck.admin.model.UserNamespace;
import com.github.nk.klusterfuck.admin.services.KubeService;
import com.github.nk.klusterfuck.admin.services.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

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

	@Autowired
	private UsersService usersService;
	@Autowired
	private KubeService kubeService;

	private ObjectMapper mapper = new ObjectMapper();

	@PostConstruct
	public void init() {
		/*
		Check for the required client id, if not found, try to create, else throw error
		In k8s, throwing error would restart the server, so effectively, we would wait for
		the keycloak pod to come up before starting this pod
		 */
		Keycloak kc = getKeycloakClientNoClientId();
		ClientsResource clients = kc.realm(realm).clients();
		ClientRepresentation clientRep = null;
		try {
			clientRep = clients.get(clientId).toRepresentation();
		} catch (Exception e) {
			// ignore
		}
		if (clientRep == null) {
			clientRep = new ClientRepresentation();
			clientRep.setClientId(clientId);
			clientRep.setBearerOnly(false);
			clientRep.setEnabled(true);
			clientRep.setFullScopeAllowed(true);
			clientRep.setProtocol("openid-connect");
			clientRep.setPublicClient(true);
			clientRep.setRedirectUris(Arrays.asList("*"));

			clientRep.setName(clientId);
			clients.create(clientRep);
		}
	}

	@ApiOperation("login")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public AccesstokenResponseWrapper login(@RequestBody LoginRequest loginRequest) throws Exception {
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
			AccessTokenResponse atr = mapper.readValue(response.getEntity().getContent(), AccessTokenResponse.class);
			return new AccesstokenResponseWrapper(atr);
		}
	}

	@ApiOperation("refresh")
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public AccesstokenResponseWrapper refresh(@RequestBody RefreshRequest refreshRequest, Principal principal) throws Exception {
		if (principal instanceof KeycloakAuthenticationToken) {
			KeycloakAuthenticationToken kat = (KeycloakAuthenticationToken) principal;
			KeycloakSecurityContext ksc = kat.getAccount().getKeycloakSecurityContext();
			String tokenString = ksc.getTokenString();
			String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
			HttpPost post = new HttpPost(tokenUrl);
			post.setHeader("Authorization", "Bearer " + tokenString);
			// create payload
			String payload = "client_id=" + clientId + "&grant_type=refresh_token&refresh_token="
					+ refreshRequest.getRefreshToken();
			post.setEntity(new StringEntity(payload));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(post);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != 200) {
				throw new Exception("Unexpected status code: " + statusCode);
			} else {
				AccessTokenResponse atr = mapper.readValue(response.getEntity().getContent(), AccessTokenResponse.class);
				return new AccesstokenResponseWrapper(atr);
			}
		}
		throw new Exception("Cannot refresh tokens");
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
			String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setType(CredentialRepresentation.PASSWORD);
			credential.setValue(registerRequest.getPassword());
			credential.setTemporary(false);
			client.realm(realm).users().get(userId).
					resetPassword(credential);

			User userObj = usersService.create(registerRequest.getEmail(), userId);
			List<UserNamespace> namespaces = userObj.getNamespaces();
			if (namespaces != null) {
				namespaces.stream().forEach(un -> {
					try {
						kubeService.createNamespace(un);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
			}

			return "{\"status\": \"OK\"}";
		} finally {
			client.close();
		}
	}

	@ApiOperation("whoami")
	@RequestMapping(value = "/whoami", method = RequestMethod.GET)
	public User whoami(Principal principal) {
		String email = principal.getName();
		return usersService.get(email);
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

	private Keycloak getKeycloakClientNoClientId() {
		return Keycloak.getInstance(
				authServerUrl,
				realm,
				adminUser,
				adminPassword,
				"admin-cli");
	}
}
