package com.github.nk.klusterfuck;

import com.github.nk.klusterfuck.model.Function;
import com.github.nk.klusterfuck.services.FunctionsService;
import com.github.nk.klusterfuck.services.RepoCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FunctionsController {

    @Autowired
    private FunctionsService fnService;

    @RequestMapping(value = "/functions", method = RequestMethod.GET)
    public List<Function> greeting() {
        return fnService.list();
    }

    @RequestMapping(value = "/functions", method = RequestMethod.POST)
    public Function create(@RequestParam("name") String name) throws RepoCreationException {
        return fnService.create(name);
    }
}
