package com.github.nk.klusterfuck.admin.controllers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.admin.model.Flow;
import com.github.nk.klusterfuck.admin.services.FlowsService;
import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by nk on 5/6/17.
 */
@Api("Flows")
@RestController
@RequestMapping("/api/v1/flows")
public class FlowsController {
	@Autowired
	private FlowsService flowsService;

	private ObjectMapper mapper = new ObjectMapper();
	private JavaType type = mapper.getTypeFactory().constructParametricType(DAG.class, StepRef.class);

	@ApiOperation("list")
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	public Flow[] list() {
		return flowsService.list();
	}

	@ApiOperation("get")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Flow get(@ApiParam @PathVariable("id") String id) {
		return flowsService.get(id);
	}

	@ApiOperation("create")
	@RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
	public Flow create(@RequestBody CreateFlowRequest cfr) {
		return flowsService.create(cfr.getName());
	}

	@ApiOperation(value = "delete", produces = "text/plain")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@ApiParam @PathVariable("id") String id) {
		flowsService.delete(id);
		return "{}";
	}

	@ApiOperation("getModel")
	@RequestMapping(value = "/{id}/model", method = RequestMethod.GET)
	public DAG<StepRef> getModel(@ApiParam @PathVariable("id") String id) throws Exception {
		return flowsService.getModel(id);
	}

	@ApiOperation(value = "saveModel", produces = "text/plain")
	@RequestMapping(value = "/{id}/model", method = RequestMethod.POST)
	public String saveModel(@ApiParam @PathVariable("id") String id, @RequestBody Map req) throws Exception {
		DAG<StepRef> value = mapper.convertValue(req, type);
		flowsService.validate(value);
		flowsService.saveModel(id, value);
		return "{}";
	}

	@ApiOperation(value = "deploy")
	@RequestMapping(value = "/{id}/deploy", method = RequestMethod.POST)
	public Flow deploy(@ApiParam @PathVariable("id") String id) throws Exception {
		return flowsService.deploy(id);
	}

	@ApiOperation(value = "validate", produces = "text/plain")
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public String validate(@RequestBody Map req) throws Exception {
		DAG<StepRef> value = mapper.convertValue(req, type);
		flowsService.validate(value);
		return "{}";
	}
}
