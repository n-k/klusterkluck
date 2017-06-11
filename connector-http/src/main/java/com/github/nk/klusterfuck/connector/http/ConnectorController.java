package com.github.nk.klusterfuck.connector.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by nk on 11/6/17.
 */
@RestController
@RequestMapping({"/", ""})
public class ConnectorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorController.class);

	@Value("${CALLBACK_URL}")
	private String callbackUrl;

	@RequestMapping(value = {"", "/"}, method = {RequestMethod.POST})
	public String call(@RequestBody String body) throws Exception {
		callFunction(callbackUrl, body);
		return "{\"status\":\"OK\"}";
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
}
