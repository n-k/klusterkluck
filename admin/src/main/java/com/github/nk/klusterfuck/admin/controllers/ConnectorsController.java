package com.github.nk.klusterfuck.admin.controllers;

import com.github.nk.klusterfuck.admin.model.Connector;
import com.github.nk.klusterfuck.admin.services.ConnectorsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by nk on 11/6/17.
 */
@Api("Connectors")
@RestController
@RequestMapping("/api/v1/connectors")
public class ConnectorsController {
	@Autowired
	private ConnectorsService connectorsService;

	@ApiOperation("list")
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	public Connector[] list() {
		return connectorsService.list();
	}

	@ApiOperation("get")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Connector get(@ApiParam @PathVariable("id") String id) {
		return connectorsService.get(id);
	}
}
