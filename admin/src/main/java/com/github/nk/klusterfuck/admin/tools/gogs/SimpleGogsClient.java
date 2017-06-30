package com.github.nk.klusterfuck.admin.tools.gogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Base64;

/**
 * Created by nk on 20/6/17.
 */
public class SimpleGogsClient {

	private static ObjectMapper mapper = new ObjectMapper();

	public static Repository createRepo(CreateRepositoryRequest crr, String url, String user, String password) throws Exception {
		String apiUrl = url + "/api/v1/user/repos";
		HttpPost post = new HttpPost(apiUrl);
		// create payload
		String payload = mapper.writeValueAsString(crr);
		post.setEntity(new StringEntity(payload));
		post.setHeader("Content-Type", "application/json");
		// create basic token
		byte[] base64auth = Base64.getEncoder().encode((user + ":" + password).getBytes());
		post.setHeader("Authorization", "Basic " + new String(base64auth));

		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(post);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (!(statusCode > 199 && statusCode < 300)) {
			throw new Exception("Unexpected status code: " + statusCode);
		} else {
			Repository repo = mapper.readValue(response.getEntity().getContent(), Repository.class);
			return repo;
		}
	}

	public static void deleteRepo(String repo, String url, String user, String password) throws Exception {
		if (true) {return;}
		String apiUrl = url + "/api/v1/repos/" + user + "/" + repo;
		HttpDelete delete = new HttpDelete(apiUrl);
		byte[] base64auth = Base64.getEncoder().encode((user + ":" + password).getBytes());
		delete.setHeader("Authorization", "Basic " + new String(base64auth));
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(delete);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (!(statusCode > 199 && statusCode < 300)) {
			throw new Exception("Unexpected status code: " + statusCode);
		}
	}
}
