package com.github.nk.klusterfuck.admin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.admin.services.ConnectorsService;

import static spark.Spark.get;
/**
 * Created by nk on 11/6/17.
 */
public class ConnectorsController {

	private ConnectorsService connectorsService;
	private ObjectMapper mapper = new ObjectMapper();

	public ConnectorsController(ConnectorsService connectorsService) {
		this.connectorsService = connectorsService;

		get("/api/v1/connectors",
				(req, res) ->
					mapper
							.writerWithDefaultPrettyPrinter()
							.writeValueAsString(connectorsService.list()));

		get("/api/v1/connectors/:id", (req, res) -> {
			String id = req.params("id");
			return mapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(connectorsService.get(id));
		});
	}

}
