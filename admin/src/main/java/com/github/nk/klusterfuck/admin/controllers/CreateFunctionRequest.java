package com.github.nk.klusterfuck.admin.controllers;

import com.github.nk.klusterfuck.admin.services.ServiceType;

/**
 * Created by nk on 3/6/17.
 */
public class CreateFunctionRequest {

	private String name;
	private String template;
	private ServiceType serviceType;
	private boolean ingress;
	private String host;
	private String path;

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public boolean isIngress() {
		return ingress;
	}

	public void setIngress(boolean ingress) {
		this.ingress = ingress;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}
