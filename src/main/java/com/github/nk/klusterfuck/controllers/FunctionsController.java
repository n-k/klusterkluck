package com.github.nk.klusterfuck.controllers;

import com.github.nk.klusterfuck.KubeConfigType;
import com.github.nk.klusterfuck.model.KFFunction;
import com.github.nk.klusterfuck.services.FunctionsService;
import com.github.nk.klusterfuck.services.RepoCreationException;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/functions")
public class FunctionsController {

    @Value("${app.kube.configType:env}")
    private KubeConfigType configType;
    @Autowired
    private DefaultKubernetesClient client;

    @Autowired
    private FunctionsService fnService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<KFFunction> greeting() {
        return fnService.list();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public KFFunction create(
            @RequestParam("name") String name,
            @RequestParam("gogs") String gogs
    ) throws RepoCreationException {
        return fnService.create(name, gogs);
    }

    @RequestMapping(value = "/{id}/run", method = RequestMethod.POST)
    public Object run(@PathVariable("id") String id, @RequestBody String payload) throws RepoCreationException, InterruptedException {
        KFFunction fn = fnService.get(id);
        // depending on where we are running, communicate with deployment
        switch (configType) {
            case env:
                throw new RuntimeException("Not supported yet");
            case url:
            case kubeconf:
                PodList podList = client.inNamespace("default").pods()
                        .withLabels(new HashMap<String, String>() {{
                            put("app", fn.getDeployment());
                        }})
                        .list();
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
                    ExecWatch execWatch = client.inNamespace("default").pods()
                            .withName(runningPod.getMetadata().getName())
                            .writingOutput(outputBuffer)
                            .writingError(errorBuffer)
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
