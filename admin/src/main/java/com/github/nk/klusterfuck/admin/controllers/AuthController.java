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
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
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

    public static class LoginRequest {
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

    private AuthzClient client() {
        Configuration config = new Configuration(
                "http://localhost:8080/auth",
                "SpringBoot",
                "product-app",
                new HashMap<String, Object>() {{put("secret", "");}},
                null);
        return AuthzClient.create(config);
    }
}
