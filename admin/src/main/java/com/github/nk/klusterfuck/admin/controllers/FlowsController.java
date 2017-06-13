package com.github.nk.klusterfuck.admin.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.admin.services.FlowsService;
import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;

import java.util.Map;

import static spark.Spark.*;

/**
 * Created by nk on 5/6/17.
 */
public class FlowsController {
	private FlowsService flowsService;
	private ObjectMapper mapper = new ObjectMapper();
	private JavaType type = mapper.getTypeFactory().constructParametricType(DAG.class, StepRef.class);

	public FlowsController(FlowsService flowsService) {
		this.flowsService = flowsService;

		path("/api/v1/flows", () -> {
			after("/*", (req, res) -> {
				res.type("application/json");
			});

			get("", (req, res) -> {
				return write(flowsService.list());
			});

			get("/:id", (req, res) -> {
				String id = req.params("id");
				return write(flowsService.get(id));
			});

			post("", (req, res) -> {
				CreateFlowRequest cfr = mapper.readValue(req.bodyAsBytes(), CreateFlowRequest.class);
				return write(flowsService.create(cfr.getName()));
			});

			delete("/:id", (req, res) -> {
				flowsService.delete(req.params("id"));
				return "{}";
			});

			get("/:id/model", (req, res) -> {
				return write(flowsService.getModel(req.params("id")));
			});

			post("/:id/model", (req, res) -> {
				Map map = mapper.readValue(req.bodyAsBytes(), Map.class);
				DAG<StepRef> value = mapper.convertValue(map, type);
				flowsService.validate(value);
				flowsService.saveModel(req.params("id"), value);
				return "{}";
			});

			post("/:id/deploy", (req, res) -> {
				return write(flowsService.deploy(req.params("id")));
			});

			post("/validate", (req, res) -> {
				Map map = mapper.readValue(req.bodyAsBytes(), Map.class);
				DAG<StepRef> value = mapper.convertValue(map, type);
				flowsService.validate(value);
				return "{}";
			});
		});

	}

	private String write(Object o) throws JsonProcessingException {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
	}

}
