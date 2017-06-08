package com.github.nk.klusterfuck.admin.services;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.IngressRule;
import io.fabric8.kubernetes.api.model.extensions.IngressRuleBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by nipunkumar on 28/05/17.
 */
@Service
public class KubeService {

	@Value("${AGENT_IMAGE}")
	private String agentImage;
	@Value("${FLOW_IMAGE}")
	private String flowImage;
	@Value("${NAMESPACE}")
	private String namespace;

	@Autowired
	private DefaultKubernetesClient client;

	public String getAgentImage() {
		return agentImage;
	}

	public String getFlowImage() {
		return flowImage;
	}

	public String getNamespace() {
		return namespace;
	}

	// @formatter:off
	public void updateDeployment(String deploymentName, String commitId) {
		client.inNamespace(namespace)
				.extensions().deployments()
				.withName(deploymentName)
				.edit()
				.editSpec()
				.editTemplate()
				.editSpec()
				.editContainer(0)
				.editEnv(4)
				.withValue(commitId)
				.endEnv()
				.endContainer()
				.endSpec()
				.endTemplate()
				.endSpec()
				.done();
	}
	// @formatter:on

	// @formatter:off
	public KubeDeployment createFnService(ServiceCreationConfig config) {
		String name = config.getName();
		ServiceType serviceType = config.getServiceType();
		io.fabric8.kubernetes.api.model.Service service = client.services().createOrReplaceWithNew()
				.withNewMetadata()
				.withName(name)
				.withNamespace(namespace)
				.withLabels(new HashMap<String, String>() {{
					put("app", name);
				}})
				.endMetadata()
				.withNewSpec()
				.withType(serviceType.name())
				.withSelector(new HashMap<String, String>() {{
					put("app", name);
				}})
				.withPorts(new ServicePort("http", null, 80, "TCP", new IntOrString(5000)))
				.endSpec()
				.done();
		Deployment deployment = client.extensions().deployments().createOrReplaceWithNew()
				.withNewMetadata()
				.withName(name)
				.withNamespace(namespace)
				.withLabels(new HashMap<String, String>() {{
					put("app", name);
				}})
				.endMetadata()
				.withNewSpec()
				.withReplicas(1)
				.withNewSelector()
				.addToMatchLabels("app", name)
				.endSelector()
				.withNewTemplate()
				.withNewMetadata()
				.withLabels(new HashMap<String, String>() {{
					put("app", name);
				}})
				.endMetadata()
				.withNewSpec()
				.withContainers()
				.addNewContainer()
				.withName("meh")
				.withImage(config.getImage())
				.withImagePullPolicy("IfNotPresent")
				.withEnv(
						new EnvVar("WORK_DIR", "/app/repo", null),
						new EnvVar("GIT_URL", config.getGitUrl(), null),
						new EnvVar("GOGS_USER", config.getGitUser(), null),
						new EnvVar("GOGS_PASSWORD", config.getGitPassword(), null),
						new EnvVar("GIT_COMMIT", config.getCommitId(), null))
				.withPorts()
				.addNewPort().withProtocol("TCP").withContainerPort(5000).endPort()
				.endContainer()
				.endSpec()
				.endTemplate()
				.endSpec()
				.done();
		if (config.isIngress()) {
			IngressRule ingressRule = new IngressRuleBuilder()
					.withHost(config.getHost())
					.withNewHttp()
					.withPaths()
					.addNewPath()
					.withPath(config.getPath())
					.withNewBackend()
					.withServiceName(name)
					.withServicePort(new IntOrString(5000))
					.endBackend()
					.endPath()
					.endHttp()
					.build();
			client.inNamespace(namespace).extensions().ingresses()
					.createNew()
					.withNewMetadata()
					.withName(name)
					.withLabels(new HashMap<String, String>() {{
						put("app", name);
					}})
					.endMetadata()
					.withNewSpec()
					.withRules(ingressRule)
					.endSpec()
					.done();
		}
		KubeDeployment kd = new KubeDeployment();
		kd.setNamespace(deployment.getMetadata().getNamespace());
		kd.setService(service.getMetadata().getName());
		kd.setDeployment(deployment.getMetadata().getName());
		return kd;
	}

	public void deleteService(String namespace, String service) {
		client.inNamespace(namespace)
				.services()
				.withName(service)
				.delete();
	}

	public void deleteDeployment(String namespace, String deployment) {
		// deleting deployment may not delete the pods, so first scale then delete
		client.inNamespace(namespace)
				.extensions()
				.deployments()
				.withName(deployment)
				.scale(0);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		client.inNamespace(namespace)
				.extensions()
				.deployments()
				.withName(deployment)
				.delete();
	}
	// @formatter:on
}
