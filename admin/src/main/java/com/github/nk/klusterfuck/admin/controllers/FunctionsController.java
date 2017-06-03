package com.github.nk.klusterfuck.admin.controllers;

import com.github.nk.klusterfuck.admin.model.KFFunction;
import com.github.nk.klusterfuck.admin.services.FunctionsService;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("Functions")
@RestController()
@RequestMapping("/api/v1/functions")
public class FunctionsController {

    @Autowired
    private FunctionsService fnService;
    @Autowired
    private DefaultKubernetesClient client;

    @ApiOperation(value = "list")
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    public List<KFFunction> list() {
        return fnService.list();
    }

    @ApiOperation(value = "create")
    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public KFFunction create(@RequestBody CreateFunctionRequest req) throws Exception {
        return fnService.create(req.getName());
    }

    @ApiOperation(value = "get")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public KFFunction get(@ApiParam @PathVariable("id") String id) {
        long idL = Long.parseLong(id);
        return fnService.list().stream()
                .filter(f -> f.getId().equals(idL))
                .findAny()
                .get();
    }

    @ApiOperation(value = "getAddress")
    @RequestMapping(value = "/{id}/service", method = RequestMethod.GET)
    public Service getService(@ApiParam @PathVariable("id") String id) {
        KFFunction fn = get(id);
        if (fn.getNamespace() == null || fn.getService() == null) {
            throw new RuntimeException("No k8s service for function");
        }
        Service service = client.inNamespace(fn.getNamespace())
                .services()
                .withName(fn.getService())
                .get();
        return service;
    }

    @ApiOperation(value = "delete", produces = "text/plain")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@ApiParam @PathVariable("id") String id) {
        // TODO:
    }
}
