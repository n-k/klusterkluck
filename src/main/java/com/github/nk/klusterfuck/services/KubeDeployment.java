package com.github.nk.klusterfuck.services;

/**
 * Created by nipunkumar on 28/05/17.
 */
public class KubeDeployment {

    private String namespace;
    private String deployment;
    private String service;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDeployment() {
        return deployment;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
