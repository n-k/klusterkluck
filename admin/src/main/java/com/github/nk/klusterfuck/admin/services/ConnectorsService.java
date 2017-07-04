package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.model.Connector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nk on 11/6/17.
 */
@Transactional
@Service
public class ConnectorsService {
	@PersistenceContext
	private EntityManager em;

	private List<Connector> defaults = new ArrayList<>();

	@PostConstruct
	public void init() {
		Connector httpConnector = new Connector();
		httpConnector.setId(1L);
		httpConnector.setDisplayName("http");
		httpConnector.setExposed(true);
		httpConnector.setImage("klusterfuck/connector-http:0.2.1");
		httpConnector.setPort(8080);
		defaults.add(httpConnector);
	}

	public Connector[] list() {
		return defaults.toArray(new Connector[0]);
	}

	public Connector get(String id) {
		return defaults.stream()
				.filter(c -> c.getDisplayName().equals(id))
				.findFirst()
				.get();
	}
}
