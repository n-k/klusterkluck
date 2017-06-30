package com.github.nk.klusterfuck.authproxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Session;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class AuthProxyApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthProxyApplication.class);

	public static void main(String[] args) {
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
				valid = true;
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
			session.attribute("uid", params.get("username"));
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
