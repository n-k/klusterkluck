package com.github.nk.klusterfuck.flow;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.common.ConnectorRef;
import com.github.nk.klusterfuck.common.FunctionRef;
import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;
import com.github.nk.klusterfuck.common.dag.Link;
import com.github.nk.klusterfuck.common.dag.Node;
import org.springframework.stereotype.Service;

/**
 * Created by nk on 6/6/17.
 */
@Service
public class DAGService {

	public DAG<StepRef> getDag() throws Exception {
		DAG<StepRef> dag = new DAG<>();
		dag.addNode(new Node<StepRef>("1", new ConnectorRef()));
		dag.addNode(new Node<StepRef>("2",
				new FunctionRef("http://10.111.72.125")));
		dag.addLink(new Link("1", "2"));
		ObjectMapper mapper = new ObjectMapper();
		String dagStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dag);
		System.out.println(dagStr);

		JavaType type = mapper.getTypeFactory().constructParametricType(DAG.class, StepRef.class);
		Object readValue = mapper.readValue(dagStr, type);
		return dag;
	}


	public static void main(String[] args) throws Exception {
		new DAGService().getDag();
	}
}
