package com.github.nk.klusterfuck.admin.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nk.klusterfuck.admin.model.Flow;
import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by nk on 9/6/17.
 */
@Service
@Transactional
public class FlowsService {
	@PersistenceContext
	private EntityManager em;
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
		f.setName(name);
		f.setContents("{}");
		em.persist(f);
		return f;
	}

	public void delete(String id) {
		Flow flow = get(id);
		em.remove(flow);
	}

	public DAG<StepRef> getModel(String id) throws Exception {
		Flow flow = get(id);
		return mapper.readValue(flow.getContents(), type);
	}

	public void saveModel(String id, DAG<StepRef> dag) throws Exception {
		Flow flow = get(id);
		flow.setContents(mapper.writeValueAsString(dag));
	}
}
