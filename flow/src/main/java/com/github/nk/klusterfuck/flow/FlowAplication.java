package com.github.nk.klusterfuck.flow;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;

import static spark.Spark.*;

import java.io.File;


/**
 * Created by nk on 6/6/17.
 */
public class FlowAplication {

	public static void main(String[] args) throws Exception {
		String confDir = getEnv("CONF_DIR");
		ObjectMapper mapper = new ObjectMapper();
		JavaType type = mapper.getTypeFactory().constructParametricType(DAG.class, StepRef.class);
		DAG<StepRef> dag = mapper.readValue(new File(confDir, "flow.json"), type);

		DAGProcessor processor = new DAGProcessor();
		processor.init();

		port(8080);

		get("/", (req, res) -> "OK");

		post("/api/v1/flow/:id", (req, res) -> {
			String id = req.params("id");
			processor.enqueue(id, dag, req.body());

			res.status(202);
			return "";
		});
	}

	private static String getEnv(String name) {
		String env = System.getenv(name);
		if (env == null) {
			throw new RuntimeException("No value for required env var " + name);
		}
		return env;
	}
}
