package com.github.nk.klusterfuck.flow;

import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nk on 6/6/17.
 */
@RestController
@RequestMapping("/api/v1/flow")
public class FlowController {

	@Autowired
	private DAGService dagService;
	@Autowired
	private DAGProcessor processor;

	private DAG<StepRef> dag;

	@PostConstruct
	public void init() throws Exception {
		dag = dagService.getDag();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public void runStep(
			@PathVariable String id,
            @RequestBody String payload,
            HttpServletResponse response) {
		processor.enqueue(id, dag, payload);
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
	}
}
