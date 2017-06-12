package com.github.nk.klusterfuck.admin.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.admin.model.KFFunction;
import com.github.nk.klusterfuck.admin.services.FunctionsService;
import com.github.nk.klusterfuck.admin.services.RepoTemplates;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static spark.Spark.*;

/**
 * Controller for /api/v1/functions/** endpoints
 */
//@Api("Functions")
//@RestController()
//@RequestMapping("/api/v1/functions")
public class FunctionsController {

	private FunctionsService fnService;
	private DefaultKubernetesClient client;
	private ObjectMapper mapper = new ObjectMapper();

	public FunctionsController(
			FunctionsService fnService,
			DefaultKubernetesClient client) {
		this.fnService = fnService;
		this.client = client;

		path("/api/v1/functions", () -> {
			after("/*", (req, res) -> {
				res.type("application/json");
			});

			get("", (req, res) -> {
				return write(fnService.list());
			});

			get("/:id", (req, res) -> {
				return write(fnService.get(req.params("id")));
			});

			post("", (req, res) -> {
				CreateFunctionRequest cfr = mapper.readValue(req.bodyAsBytes(), CreateFunctionRequest.class);
				KFFunction kfFunction = fnService.create(cfr, RepoTemplates.getFunctionInitializer());
				return write(kfFunction);
			});

			get("/:id/versions", (req, res) -> {
				return write(fnService.getCommits(req.params("id")));
			});

			get("/:id/versions/:versionId", (req, res) -> {
				return write(
						fnService.getCommits(req.params("id"))
								.stream()
								.filter(v -> v.getId().equals(req.params("versionId")))
								.findFirst()
								.get());
			});

			get("/:id/versions/:versionId", (req, res) -> {
				fnService.setVersion(req.params("id"), req.params("versionId"));
				return "{\"status\":\"OK\"}";
			});

			get("/:id/service", (req, res) -> {
				KFFunction fn = fnService.get(req.params("id"));
				if (fn.getNamespace() == null || fn.getService() == null) {
					throw new RuntimeException("No k8s service for function");
				}
				Service service = client.inNamespace(fn.getNamespace())
						.services()
						.withName(fn.getService())
						.get();
				return write(service);
			});

			delete("/:id", (req, res) -> {
				fnService.delete(req.params("id"));
				return "{}";
			});

			post("/:id/proxy", (req, res) -> {
				KFFunction fn = fnService.get(req.params("id"));
				if (fn.getNamespace() == null || fn.getService() == null) {
					throw new RuntimeException("No k8s service for function");
				}
				Service service = client.inNamespace(fn.getNamespace())
						.services()
						.withName(fn.getService())
						.get();
				String clusterIP = service.getSpec().getClusterIP();
				HttpClient httpClient = HttpClientBuilder.create().build();

				HttpPost post = new HttpPost("http://" + clusterIP);
				post.setEntity(new StringEntity(req.body()));
				post.setHeader("Content-Type", "text/plain");

				HttpResponse response = httpClient.execute(post);
				ProxyResponse proxyResponse = new ProxyResponse();
				proxyResponse.setCode(response.getStatusLine().getStatusCode());
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				proxyResponse.setBody(result.toString());
				return write(proxyResponse);
			});
		});
	}

	public static class ProxyResponse {
		private int code;
		private String body;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}
	}

	private String write(Object o) throws JsonProcessingException {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
	}
}
