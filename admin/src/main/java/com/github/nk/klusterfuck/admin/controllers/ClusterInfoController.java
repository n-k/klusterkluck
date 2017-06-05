package com.github.nk.klusterfuck.admin.controllers;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by nk on 5/6/17.
 */
@Api("Cluster Info")
@RestController
@RequestMapping("/api/v1/cluster")
public class ClusterInfoController {

	@Autowired
	private DefaultKubernetesClient client;

	@ApiOperation("listNodes")
	@RequestMapping(value = "/nodes", method = RequestMethod.GET)
	public List<Node> listNodes() {
		return client.nodes().list().getItems();
	}

	@ApiOperation("getNode")
	@RequestMapping(value = "/nodes/{name}", method = RequestMethod.GET)
	public Node getNode(@ApiParam @PathVariable("name") String name) {
		return client.nodes().list().getItems().stream()
				.filter(n -> n.getMetadata().getName().equalsIgnoreCase(name))
				.findFirst()
				.get();
	}
}
