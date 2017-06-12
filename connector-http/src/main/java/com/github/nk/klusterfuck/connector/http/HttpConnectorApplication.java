package com.github.nk.klusterfuck.connector.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static spark.Spark.*;

public class HttpConnectorApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnectorApplication.class);

	public static void main(String[] args) {
		String callbackUrl = getEnv("CALLBACK_URL");

		port(8080);

		get("/", (req, res) -> "OK");

		post("/", (req, res) -> {
			callFunction(callbackUrl, req.body());
			res.type("application/json");
			return "{\"status\":\"OK\"}";
		});
	}

	private static String callFunction(String url, String payload) throws Exception {
		LOGGER.info("Calling " + url + " with payload " + payload);
		HttpClient client = HttpClientBuilder.create().build();

		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(payload));
		post.setHeader("Content-Type", "text/plain");

		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		LOGGER.info("Recieved from " + url + ": " + result);
		return result.toString();
	}

	private static String getEnv(String name) {
		String env = System.getenv(name);
		if (env == null) {
			throw new RuntimeException("No value for required env var " + name);
		}
		return env;
	}
}
