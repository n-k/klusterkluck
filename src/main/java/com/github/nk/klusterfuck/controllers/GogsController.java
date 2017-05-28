package com.github.nk.klusterfuck.controllers;

import com.github.nk.klusterfuck.KubeConfigType;
import com.github.nk.klusterfuck.model.GogsConnection;
import com.github.nk.klusterfuck.services.GogsService;
import com.github.nk.klusterfuck.services.IdService;
import com.github.nk.klusterfuck.services.KubeDeployment;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.extensions.*;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nipunkumar on 28/05/17.
 */
@RestController()
@RequestMapping("/api/v1/gogs")
public class GogsController {
    @Value("${app.kube.configType:env}")
    private KubeConfigType configType;
    @Value("${NAMESPACE:default}")
    private String namespace;

    @Autowired
    private DefaultKubernetesClient client;
    @Autowired
    private GogsService gogsService;
    @Autowired
    private IdService idService;

    @RequestMapping(value = "/setup", method = RequestMethod.POST)
    public void setupTestGogs(
            @RequestParam(name = "ingress", required = false, defaultValue = "false") boolean ingress,
            @RequestParam(name = "ingressHost", required = false) String ingressHost
    ) {
        try {
            if (get("default") != null) {
                throw new RuntimeException("Defalt gogs connection exists, delete before creating again");
            }
        } catch (NoResultException nre) {
            // ignore
        }
        String username = "gogsadmin";
        String password = idService.newId();
        KubeDeployment defaultGogs = createDefaultGogs(username, password, ingress, ingressHost);
        GogsConnection connection = new GogsConnection();
        connection.setName("default");
        if (ingress) {
            connection.setExternalUrl("http://" + ingressHost);
        }
        connection.setUrl("http://" + defaultGogs.getServiceIP() + ":3000");
        connection.setUsername(username);
        connection.setPassword(password);
        gogsService.save(connection);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<GogsConnection> list() {
        return gogsService.list();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public GogsConnection create(@RequestBody GogsConnection connection) {
        if ("default".endsWith(connection.getName())) {
            throw new RuntimeException("Connection name default is reserved");
        }
        return gogsService.save(connection);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public GogsConnection get(@PathVariable("id") String id) {
        return gogsService.get(id);
    }

    // @formatter:off
    public KubeDeployment createDefaultGogs(String user, String password, boolean ingress, String ingressHost) {
        String name = "gogs";
        io.fabric8.kubernetes.api.model.Service service = client.services().createNew()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                    .withLabels(new HashMap<String, String>() {{put("app", name);}})
                .endMetadata()
                .withNewSpec()
                    .withSelector(new HashMap<String, String>() {{
                        put("app", name);
                    }})
                    .withPorts(new ServicePort("http", null, 3000, "TCP", new IntOrString(3000)))
                .endSpec()
                .done();
        String domain = "localhost";
        Deployment deployment = client.extensions().deployments().createNew()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                    .withLabels(new HashMap<String, String>() {{put("app", name);}})
                .endMetadata()
                .withNewSpec()
                    .withReplicas(1)
                    .withNewSelector()
                        .addToMatchLabels("app", name)
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .withLabels(new HashMap<String, String>() {{put("app", name);}})
                        .endMetadata()
                        .withNewSpec()
                            .withContainers()
                                .addNewContainer()
                                    .withName("gogs")
                                    .withImage("fabric8/gogs")
                                    .withImagePullPolicy("Always")
                                    .withEnv()
                                        .withEnv(
                                                new EnvVar("ADMIN_USER_NAME", user, null),
                                                new EnvVar("ADMIN_USER_PASSWORD", password, null),
                                                new EnvVar("DOMAIN", domain, null))
                                    .withPorts()
                                        .addNewPort().withProtocol("TCP").withContainerPort(3000).endPort()
                                .endContainer()
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .done();
        if (ingress) {
            IngressRule ingressRule = new IngressRuleBuilder()
                    .withHost(ingressHost)
                    .withNewHttp()
                        .withPaths(new HTTPIngressPath(new IngressBackend(name, new IntOrString(3000)), "/"))
                    .endHttp()
                    .build();
            client.extensions().ingresses().createNew()
                    .withNewMetadata()
                        .withName(name)
                        .withNamespace(namespace)
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
        kd.setServiceIP(service.getSpec().getClusterIP());
        return kd;
    }
    // @formatter:on
}
