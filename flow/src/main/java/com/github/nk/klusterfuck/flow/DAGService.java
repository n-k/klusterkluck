package com.github.nk.klusterfuck.flow;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * Created by nk on 6/6/17.
 */
@Service
public class DAGService {
	@Value("${CONF_DIR}")
	private String confDir;

	private DAG<StepRef> dag;

	@PostConstruct
	public void init() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JavaType type = mapper.getTypeFactory().constructParametricType(DAG.class, StepRef.class);
		dag = mapper.readValue(new File(confDir, "flow.json"), type);
	}

	public DAG<StepRef> getDag() throws Exception {
		return dag;
	}

}
