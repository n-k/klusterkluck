package com.github.nk.klusterfuck.admin.controllers;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.nk.klusterfuck.admin.KubeConfigType;
import com.github.nk.klusterfuck.admin.model.KFFunction;
import com.github.nk.klusterfuck.admin.services.FunctionsService;
import com.github.nk.klusterfuck.admin.services.RepoCreationException;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;

@RestController()
@RequestMapping("/api/v1/functions")
public class FunctionsController {

	@Value("${app.kube.configType:env}")
	private KubeConfigType configType;
	@Value("${NAMESPACE:default}")
	private String namespace;
	@Autowired
	private DefaultKubernetesClient client;

	@Autowired
	private FunctionsService fnService;

	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	public List<KFFunction> greeting() {
		return fnService.list();
	}

	@RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
	public KFFunction create(@RequestParam("name") String name) throws Exception {
		return fnService.create(name);
	}

	@RequestMapping(value = "/{id}/run", method = RequestMethod.POST)
	public Object run(@PathVariable("id") String id, @RequestBody String payload)
			throws RepoCreationException, InterruptedException {
		KFFunction fn = fnService.get(id);
		switch (configType) {
		case env:
		case url:
		case kubeconf:
			PodList podList = client.inNamespace(namespace).pods().withLabels(new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;

				{
					put("app", fn.getDeployment());
				}
			}).list();
			List<Pod> pods = podList.getItems();
			if (pods.size() < 1) {
				throw new RuntimeException("No pods found");
			}
			// get running pods
			Pod runningPod = null;
			for (Pod pod : pods) {
				System.out.println(pod.getStatus());
				if ("Running".equals(pod.getStatus().getPhase())) {
					runningPod = pod;
					break;
				}
			}
			if (runningPod == null) {
				throw new RuntimeException("No running pods found");
			} else {
				if (payload == null) {
					payload = "";
				}
				ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
				ByteArrayOutputStream errorBuffer = new ByteArrayOutputStream();
				ExecWatch execWatch = client.inNamespace("default").pods().withName(runningPod.getMetadata().getName())
						.writingOutput(outputBuffer).writingError(errorBuffer)
						.exec("curl", "-v", "localhost:5000", "-d{\"payload\":\"" + payload + "\"}");
				Thread.sleep(1000);
				execWatch.close();
				System.err.println(new String(errorBuffer.toByteArray()));
				return new String(outputBuffer.toByteArray());
			}
		}
		return null;
	}
}
