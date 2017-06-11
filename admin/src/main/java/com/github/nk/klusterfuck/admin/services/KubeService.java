package com.github.nk.klusterfuck.admin.services;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.IngressRule;
import io.fabric8.kubernetes.api.model.extensions.IngressRuleBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
	@Autowired
	private IdService idService;

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
	public void updateFnDeployment(String deploymentName, String commitId) {
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

	public void clean(String name) {
		client.inNamespace(namespace).configMaps().withName(name).delete();
		client.inNamespace(namespace).services().withName(name).delete();
		client.inNamespace(namespace).extensions().deployments().withName(name).delete();
	}

	public void deleteConfigmap(String name) {
		client.inNamespace(namespace).configMaps().withName(name).delete();
	}

	public ConfigMap createConfigMap(String name, Map<String, String> contents) {
		return client.inNamespace(namespace).configMaps().createNew()
				.withNewMetadata().withName(name).endMetadata()
				.withData(contents)
				.done();
	}

	public io.fabric8.kubernetes.api.model.Service findService(String name) {
		return client.inNamespace(namespace).services().withName(name).get();
	}

	public Deployment findDeployment(String name) {
		return client.inNamespace(namespace).extensions().deployments().withName(name).get();
	}

	public void deleteServices(Map<String, String> labels) {
		client.inNamespace(namespace).services().withLabels(labels).delete();
	}

	public void deleteDeployments(Map<String, String> labels) {
		client.inNamespace(namespace).extensions().deployments().withLabels(labels).delete();
	}

	public void updateDeployment(String name) {
		client.inNamespace(namespace).extensions().deployments().withName(name)
				.edit()
				.editMetadata()
				.removeFromLabels("uuid")
				.addToLabels("uuid", idService.newId())
				.endMetadata()
				.done();
	}

	public io.fabric8.kubernetes.api.model.Service
	createService(String name, Map<String, String> labels, int port) {
		return client.inNamespace(namespace).services()
				.createNew()
				.withNewMetadata()
				.withName(name)
				.withLabels(labels)
				.endMetadata()
				.withNewSpec()
				.withType("ClusterIP")
				.withSelector(labels)
				.withPorts(
						new ServicePort(
								"http",
								null,
								80,
								"TCP",
								new IntOrString(port)))
				.endSpec()
				.done();
	}

	private static class ConfMapVol {
		private String mountPath;
		private String confmap;
		private String name;
	}

	public Deployment createDeployment(
			String name,
			String image,
			Map<String, String> labels,
			int port,
			Map<String, String> env,
			Map<String, String> configMapVols) {
		List<ConfMapVol> volsConfs = new ArrayList<>();
		for (Map.Entry<String, String> e : configMapVols.entrySet()) {
			ConfMapVol vol = new ConfMapVol();
			vol.name = "confvolmount-" + idService.newId();
			vol.mountPath = e.getKey();
			vol.confmap = e.getValue();
			volsConfs.add(vol);
		}
		// need list of volumes and volume mounts
		List<Volume> vols = new ArrayList<>();
		for (ConfMapVol vol : volsConfs) {
			Volume volume = new VolumeBuilder().withConfigMap(
					new ConfigMapVolumeSource(511, null, vol.confmap))
					.withName(vol.name)
					.build();
			vols.add(volume);
		}
		List<VolumeMount> mounts = new ArrayList<>();
		for (ConfMapVol vol : volsConfs) {
			VolumeMount mount = new VolumeMountBuilder()
					.withMountPath(vol.mountPath)
					.withName(vol.name)
					.build();
			mounts.add(mount);
		}
		return client.inNamespace(namespace).extensions().deployments()
				.createNew().withNewMetadata()
					.withName(name)
					.withNamespace(namespace)
					.withLabels(labels)
				.endMetadata()
				.withNewSpec()
					.withReplicas(1)
					.withNewSelector()
					.withMatchLabels(labels)
					.endSelector()
					.withNewTemplate()
						.withNewMetadata()
							.withLabels(labels)
						.endMetadata()
						.withNewSpec().withContainers()
							.addNewContainer().withImage(image).withName("main")
							.withImagePullPolicy("IfNotPresent")
							.withEnv(
									env.entrySet().stream()
											.map(e -> new EnvVar(e.getKey(), e.getValue(), null))
											.collect(Collectors.toList()))
							.withPorts().addNewPort().withProtocol("TCP").withContainerPort(port).endPort()
							.withVolumeMounts(mounts.toArray(new VolumeMount[0]))
							.withResources(
									new ResourceRequirements(new HashMap<String, Quantity>(){{
										put("cpu", new Quantity("200m"));
										put("memory", new Quantity("200Mi"));
									}}, null))
							.endContainer()
							.withVolumes(vols.toArray(new Volume[0]))
						.endSpec()
					.endTemplate()
				.endSpec()
				.done();
	}

	public KubeDeployment createFnService(ServiceCreationConfig config) {
		String name = config.getName();
		io.fabric8.kubernetes.api.model.Service service = client.services().createOrReplaceWithNew()
				.withNewMetadata()
				.withName(name)
				.withNamespace(namespace)
				.withLabels(new HashMap<String, String>() {{
					put("app", name);
				}})
				.endMetadata()
				.withNewSpec()
				.withType("ClusterIP")
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
				.withPorts().addNewPort().withProtocol("TCP").withContainerPort(5000).endPort()
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
