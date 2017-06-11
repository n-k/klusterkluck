package com.github.nk.klusterfuck.admin.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.admin.model.Connector;
import com.github.nk.klusterfuck.admin.model.Flow;
import com.github.nk.klusterfuck.admin.model.KFFunction;
import com.github.nk.klusterfuck.common.ConnectorRef;
import com.github.nk.klusterfuck.common.FunctionRef;
import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by nk on 9/6/17.
 */
@Service
@Transactional
public class FlowsService {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private IdService idService;
	@Autowired
	private FunctionsService fnService;
	@Autowired
	private ConnectorsService connService;
	@Autowired
	private KubeService kubeService;

	@Value("${FLOW_IMAGE}")
	private String flowImage;

	private ObjectMapper mapper = new ObjectMapper();
	JavaType type = mapper.getTypeFactory().constructParametricType(DAG.class, StepRef.class);

	public Flow[] list() {
		TypedQuery<Flow> query = em.createQuery("select f from Flow f", Flow.class);
		return query.getResultList().toArray(new Flow[0]);
	}

	public Flow get(String id) {
		TypedQuery<Flow> query =
				em.createQuery("select f from Flow f where f.id = :id", Flow.class);
		query.setParameter("id", Long.parseLong(id));
		return query.getSingleResult();
	}

	public Flow create(String name) {
		Flow f = new Flow();
		f.setName(idService.newId());
		f.setDisplayName(name);
		f.setContents("{}");
		em.persist(f);
		return f;
	}

	public void delete(String id) {
		Flow flow = get(id);
		em.remove(flow);
		kubeService.clean(flow.getName());
	}

	public DAG<StepRef> getModel(String id) throws Exception {
		Flow flow = get(id);
		return mapper.readValue(flow.getContents(), type);
	}

	public void saveModel(String id, DAG<StepRef> dag) throws Exception {
		Flow flow = get(id);
		flow.setContents(mapper.writeValueAsString(dag));
	}

	public Flow deploy(String id) throws Exception {
		Flow flow = get(id);
		validate(flow);
		// first deploy the flow processor, so we know the callback URLs for
		// connectors
		Map<String, String> labels = new HashMap<>();
		labels.put("app", "flow");
		labels.put("flowId", id);
		kubeService.deleteServices(labels);
		io.fabric8.kubernetes.api.model.Service service
				= kubeService.findService(flow.getName());
		if (service == null) {
			service = kubeService.createService(flow.getName(), labels, 8080);
		}

		Map<String, String> configMap = new HashMap<>();
		configMap.put("flow.json", flow.getContents());
		// recreate configmap with current value
		kubeService.deleteConfigmap(flow.getName());
		kubeService.createConfigMap(flow.getName(), configMap);

		if (kubeService.findDeployment(flow.getName()) == null) {
			Map<String, String> env = new HashMap<>();
			env.put("CONF_DIR", "/app/conf");

			Map<String, String> mounts = new HashMap<>();
			mounts.put("/app/conf", flow.getName());

			kubeService.createDeployment(
					flow.getName(),
					flowImage,
					labels,
					8080,
					env,
					mounts);
		} else {
			// update deployment, replace uuid field so deployment triggers
			kubeService.updateDeployment(flow.getName());
		}

		String flowUrl = "http://" + service.getMetadata().getName() + "."
				+ service.getMetadata().getNamespace() + ".svc.cluster.local/api/v1/flow";

		DAG<StepRef> dag = getModel(id);
		// list exiting services and deployments linked to this flow, and delete them
		// then recreate new once
		Map<String, String> commonConnectorLabels = new HashMap<>();
		commonConnectorLabels.put("app", "flow-connectors");
		commonConnectorLabels.put("flow-id", id);
		kubeService.deleteDeployments(commonConnectorLabels);
		kubeService.deleteServices(commonConnectorLabels);
		commonConnectorLabels.put("uuid", idService.newId());
		Arrays.stream(dag.getNodes())
				.filter(n -> n.getData().getCategory() == StepRef.RefType.connector)
				.forEach(n -> {
					Map<String, String> connectorLabels = new HashMap<>();
					connectorLabels.putAll(commonConnectorLabels);
					connectorLabels.put("connector", "connector_" + n.getId());

					ConnectorRef cr = (ConnectorRef) n.getData();
					String connectorId = cr.getConnectorId();
					String connServiceName = flow.getName() + "-" + n.getId();
					kubeService.createService(connServiceName, connectorLabels, 8080);

					Connector connector = connService.get(connectorId);
					String callbackUrl = flowUrl + "/" + n.getId();
					kubeService.createDeployment(
							connServiceName,
							connector.getImage(),
							connectorLabels,
							8080,
							new HashMap<String, String>() {{
								put("CALLBACK_URL", callbackUrl);
							}},
							new HashMap<>());
				});

		return flow;
	}

	public void validate(Flow flow) throws Exception {
		validate((DAG<StepRef>) mapper.readValue(flow.getContents(), type));
	}

	public void validate(DAG<StepRef> dag) throws Exception {
		/*
		Check:
		1. steps have unique ids
		2. no cycles
		3. no incoming links for connectors
		4. all functions have at least one incoming node
		 */
		Set<String> stepIds = new HashSet<>();
		Arrays.stream(dag.getNodes())
				.forEach(n -> {
					String id = n.getId();
					if (stepIds.contains(id)) {
						throw new RuntimeException("More than one node has id: " + id);
					} else {
						stepIds.add(id);
					}
				});
		Arrays.stream(dag.getLinks())
				.forEach(l -> {
					String from = l.getFrom();
					if (!stepIds.contains(from)) {
						throw new RuntimeException("Found link from non-existant node: " + from);
					}
					String to = l.getTo();
					if (!stepIds.contains(to)) {
						throw new RuntimeException("Found link to non-existant node: " + to);
					}
				});
		// getTopologicalOrdering will throw error if there are cycles
		dag.getTopologicalOrdering().stream()
				.forEach(n -> {
					switch (n.getData().getCategory()) {
						case fn:
							if (dag.getIncomingNodes(n.getId()).size() == 0) {
								throw new RuntimeException("Found no incoming links to function: " + n.getId());
							}
							FunctionRef fnRef = (FunctionRef) n.getData();
							// must have function id set
							if (fnRef.getFunctionId() == null || fnRef.getFunctionId().isEmpty()) {
								throw new RuntimeException("Function step with id " + n.getId() + " has no fn id");
							}
							KFFunction fn = fnService.get(fnRef.getFunctionId());
							if (fn == null) {
								throw new RuntimeException("No such function: " + fnRef.getFunctionId());
							}
							fnRef.setUrl("http://" + fn.getService() + "." + fn.getNamespace() + ".svc.cluster.local");
							break;
						case connector:
							if (dag.getIncomingNodes(n.getId()).size() != 0) {
								throw new RuntimeException("Found incoming link to connector: " + n.getId());
							}
							ConnectorRef connRef = (ConnectorRef) n.getData();
							if (connRef.getConnectorId() == null || connRef.getConnectorId().isEmpty()) {
								throw new RuntimeException("Connector step with id " + n.getId() + " has no connector id");
							}
							if (connService.get(connRef.getConnectorId()) == null) {
								throw new RuntimeException("No such connector: " + connRef.getConnectorId());
							}
							break;
					}
				});
	}
}
