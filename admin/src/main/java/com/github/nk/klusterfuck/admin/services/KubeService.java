package com.github.nk.klusterfuck.admin.services;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.nk.klusterfuck.admin.model.UserNamespace;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.IngressRule;
import io.fabric8.kubernetes.api.model.extensions.IngressRuleBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nipunkumar on 28/05/17.
 */
@Service
public class KubeService {

	@Value("${app.kube.agentImage}")
	private String agentImage;
	@Value("${app.kube.flowImage}")
	private String flowImage;
	@Value("${app.kube.authProxyImage}")
	private String authImage;
	@Value("${app.kube.imageVersion}")
	private String imageVersion;
	@Value("${app.domain}")
	private String domain;

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;
	@Value("${keycloak.realm}")
	private String realm;
	@Value("${keycloak.resource}")
	private String clientId;

	@Autowired
	private DefaultKubernetesClient client;
	@Autowired
	private IdService idService;

	public String getFlowImage() {
		return flowImage;
	}

	// @formatter:off
	public void updateFnDeployment(String namespace, String deploymentName, String commitId) {
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

	public void clean(String namespace, String name, Map<String, String> labels) {
		client.inNamespace(namespace).configMaps().withName(name).delete();
		client.inNamespace(namespace).services().withName(name).delete();
		client.inNamespace(namespace).extensions().deployments().withName(name).delete();
		client.inNamespace(namespace).services().withLabels(labels).delete();
		client.inNamespace(namespace).extensions().deployments().withLabels(labels).delete();
	}

	public void deleteConfigmap(String namespace, String name) {
		client.inNamespace(namespace).configMaps().withName(name).delete();
	}

	public ConfigMap createConfigMap(String namespace, String name, Map<String, String> contents) {
		return client.inNamespace(namespace).configMaps().createNew()
				.withNewMetadata().withName(name).endMetadata()
				.withData(contents)
				.done();
	}

	public io.fabric8.kubernetes.api.model.Service findService(String namespace, String name) {
		return client.inNamespace(namespace).services().withName(name).get();
	}

	public Deployment findDeployment(String namespace, String name) {
		return client.inNamespace(namespace).extensions().deployments().withName(name).get();
	}

	public void deleteServices(String namespace, Map<String, String> labels) {
		client.inNamespace(namespace).services().withLabels(labels).delete();
	}

	public void deleteDeployments(String namespace, Map<String, String> labels) {
		client.inNamespace(namespace).extensions().deployments().withLabels(labels).delete();
	}

	public void updateDeployment(String namespace, String name) {
		client.inNamespace(namespace).extensions().deployments().withName(name)
				.edit()
				.editMetadata()
				.removeFromLabels("uuid")
				.addToLabels("uuid", idService.newId())
				.endMetadata()
				.done();
	}

	public io.fabric8.kubernetes.api.model.Service
	createService(String namespace, String name, Map<String, String> labels, int port) {
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

	public void createNamespace(UserNamespace un) throws Exception {
		Namespace namespace = new NamespaceBuilder()
				.withNewMetadata()
					.withName(un.getName())
				.endMetadata()
				.build();
		client.namespaces().create(namespace);
		// create gogs and cloud9
		Map<String, Object> params = new HashMap<>();
		params.put("NAMESPACE", un.getName());
		params.put("GOGS_ADMIN_USER", un.getGitUser());
		params.put("GOGS_ADMIN_PASSWORD", un.getGitPassword());
		params.put("DOMAIN", domain);
		params.put("TAG", imageVersion);

		params.put("KEYCLOAK_URL", authServerUrl);
		params.put("REALM", realm);
		params.put("CLIENT_ID", clientId);

		applyManifest("k8s_templates/auth.yaml", params);
		// get auth service IP and set AUTH_SERVER value
		params.put("AUTH_SERVER", getServiceIP(un.getName(), "auth"));
		applyManifest("k8s_templates/gogs.yaml", params);
		applyManifest("k8s_templates/cloud9.yaml", params);
	}

	public void applyManifest(String resource, Map<String, Object> params) throws Exception {
		Handlebars handlebars = new Handlebars();
		Template template = handlebars.compileInline(readResource(resource));
		String resolved = template.apply(params);
		Arrays.stream(resolved.split("---"))
				.filter(frag -> frag != null && !frag.isEmpty())
				.forEach(frag -> {
					client.load(new ByteArrayInputStream(frag.getBytes())).createOrReplace();
				});
	}

	private String getServiceIP(String namespace, String serviceName) {
		io.fabric8.kubernetes.api.model.Service authService
				= client.inNamespace(namespace).services().withName(serviceName).get();
		return authService.getSpec().getClusterIP();
	}

	private String readResource(String resource) throws IOException {
		List<String> lines =
				IOUtils.readLines(getClass().getClassLoader().getResourceAsStream(resource), "UTF-8");
		String all = "";
		for (String line: lines) {
			all = all + line + "\n";
		}
		return all;
	}

	public String getDomain() {
		return domain;
	}

    private static class ConfMapVol {
		private String mountPath;
		private String confmap;
		private String name;
	}

	public Deployment createDeployment(
			String namespace,
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
		HashMap<String, Quantity> resourcesMap = new HashMap<String, Quantity>() {{
			put("cpu", new Quantity("100m"));
			put("memory", new Quantity("100Mi"));
		}};
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
							.withReadinessProbe(
									new ProbeBuilder()
											.withHttpGet(
													new HTTPGetActionBuilder()
															.withPath("/")
															.withPort(new IntOrString(port))
															.withScheme("HTTP")
															.build())
											.build())
							.withEnv(
									env.entrySet().stream()
											.map(e -> new EnvVar(e.getKey(), e.getValue(), null))
											.collect(Collectors.toList()))
							.withPorts().addNewPort().withProtocol("TCP").withContainerPort(port).endPort()
							.withVolumeMounts(mounts.toArray(new VolumeMount[0]))
							.withResources(
									// request full limit right away, because not sure how jvm's new cgroup memory
									// options will handle an increase at later time
									new ResourceRequirements(resourcesMap, resourcesMap))
							.endContainer()
							.withVolumes(vols.toArray(new Volume[0]))
						.endSpec()
					.endTemplate()
				.endSpec()
				.done();
	}

	public KubeDeployment createFnService(
			String namespace,
			String name,
			String gitUrl,
			String gitUser,
			String gitPassword,
			String gitCommit)
			throws Exception {
		applyManifest(
				"k8s_templates/function.yaml",
				new HashMap<String, Object>() {{
					put("IMAGE", agentImage + ":" + imageVersion);
					put("DOMAIN", domain);
					put("NAME", name);
					put("NAMESPACE", namespace);
					put("WORK_DIR", "/app/repo");
					put("GIT_URL", gitUrl);
					put("GIT_USER", gitUser);
					put("GIT_PASSWORD", gitPassword);
					put("GIT_COMMIT", gitCommit);
					put("AUTH_SERVER", getServiceIP(namespace, "auth"));
				}});
		KubeDeployment kd = new KubeDeployment();
		kd.setNamespace(namespace);
		kd.setService(name);
		kd.setDeployment(name);
		return kd;
	}

	void cloneInCloud9Pod(String namespace, String gitCloneUrl) {
		List<Pod> pods = client.inNamespace(namespace).pods()
				.withLabels(new HashMap<String, String>() {{
					put("app", "cloud9");
				}})
				.list()
				.getItems();
		if (pods.size() > 0) {
			Pod pod = pods.get(0);
			client.inNamespace(namespace).pods().withName(pod.getMetadata().getName())
					.readingInput(
							new ByteArrayInputStream(
									("sh -c 'cd /workspace && git clone " + gitCloneUrl + "'\n\n").getBytes()))
					.writingOutput(System.out)
					.writingError(System.err)
					.withTTY()
					.usingListener(new ExecListener() {
						@Override
						public void onOpen(Response response) {
							try {
								System.err.println(new String(response.body().bytes()));
							} catch (IOException e) {
							}
						}

						@Override
						public void onFailure(Throwable t, Response response) {
							t.printStackTrace();
							try {
								System.err.println(new String(response.body().bytes()));
							} catch (IOException e) {
							}
						}

						@Override
						public void onClose(int code, String reason) {
							System.err.println(reason);
						}
					}).exec();
		}
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
