package com.github.nk.klusterfuck.authproxy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Session;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class AuthProxyApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthProxyApplication.class);

	public static void main(String[] args) {
		final String authServerUrl = getEnv("KEYCLOAK_URL");
		final String realm = getEnv("REALM");
		final String clientId = getEnv("CLIENT_ID");

		port(9000);
		staticFileLocation("static/");
		get("/", (req, res) -> {
			// check for auth token, then for session
			String authHeader = req.headers("Authorization");
			boolean valid = false;
			if (authHeader != null
					&& !authHeader.isEmpty()
					&& authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring("Bearer ".length());
				// check if token is valid
				String infoUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";
				HttpGet get = new HttpGet(infoUrl);
				get.setHeader("Authorization", "Bearer " + token);
				try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
					CloseableHttpResponse response = client.execute(get);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode != 200) {
						valid = true;
					}
				}
			}
			if (!valid) {
				Session session = req.session(false);
				if (session != null) {
					Object uidObj = session.attribute("uid");
					if (uidObj != null) {
						valid = true;
					}
				}
			}
			if (!valid) {
				res.status(401);
				return "";
			}
			return "OK";
		});
		post("/signin", (req, res) -> {
			Session session = req.session(true);
			List<NameValuePair> pairs = URLEncodedUtils.parse(req.body(), Charset.defaultCharset());
			Map<String, String> params = toMap(pairs);
			String username = params.get("username");
			String password = params.get("password");
			if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
				String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
				HttpPost post = new HttpPost(tokenUrl);
				// create payload
				String payload = "scope=offline&client_id=" + clientId
						+ "&grant_type=password&username=" + username
						+ "&password=" + password;
				post.setEntity(new StringEntity(payload));
				post.setHeader("Content-Type", "application/x-www-form-urlencoded");
				try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
					HttpResponse response = client.execute(post);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode != 200) {
						HttpEntity entity = response.getEntity();
						String resp = EntityUtils.toString(entity);
						LOGGER.warn("URL: " + tokenUrl + "Status: " + statusCode + ": " + resp);
						res.status(403);
						res.header("Content-Type", "text/html");
						return "<html><body>" +
								"Invalid credentials. Try again <a href=\"./signin.html\">Login</a>" +
								"</body></html>";
					} else {
						session.attribute("uid", username);
						// send redirect to ../
						res.status(302);
						res.header("Location", "../");
						return "";
					}
				}
			}
			return "";
		});
	}

	private static Map<String, String> toMap(List<NameValuePair> pairs) {
		return pairs
				.stream()
				.collect(Collectors.toMap(p -> p.getName(), p -> p.getValue()));
	}

	private static String getEnv(String name) {
		String env = System.getenv(name);
		if (env == null) {
			throw new RuntimeException("No value for required env var " + name);
		}
		return env;
	}
}
