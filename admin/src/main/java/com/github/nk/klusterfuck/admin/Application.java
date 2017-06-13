package com.github.nk.klusterfuck.admin;

import com.github.nk.klusterfuck.admin.controllers.ConnectorsController;
import com.github.nk.klusterfuck.admin.controllers.FlowsController;
import com.github.nk.klusterfuck.admin.controllers.FunctionsController;
import com.github.nk.klusterfuck.admin.services.*;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static spark.Spark.port;
import static spark.Spark.staticFiles;

public class Application {

	public static void main(String[] args) throws Exception {
		Config config = Config.fromEnv();
		// eagerly init persistence
		PersistenceUtils.doInTxn(em -> null);
		IdService idService = new IdService();
		DefaultKubernetesClient client = kubernetesClient(config.kubeConfigType);
		KubeService kubeService = new KubeService(
				config.agentImage,
				config.flowImage,
				config.namespace,
				client,
				idService);
		GogsService gogsService = new GogsService(
				config.gogsUrl,
				config.gogsUser,
				config.gogsPassword);
		ConnectorsService connectorsService = new ConnectorsService();
		FunctionsService functionsService = new FunctionsService(
				gogsService,
				kubeService,
				idService);
		FlowsService flowsService = new FlowsService(
				idService,
				functionsService,
				connectorsService,
				kubeService,
				config.flowImage);

		port(8080);

		if (config.env == Env.dev) {
			staticFiles.externalLocation(new File("./admin/src/main/ui/dist").getCanonicalFile().getAbsolutePath());
		} else {
			staticFiles.location("/static");
		}
		new ConnectorsController(connectorsService);
		new FlowsController(flowsService);
		new FunctionsController(functionsService, client);
	}

	public static DefaultKubernetesClient kubernetesClient(KubeConfigType configType) throws IOException {
		switch (configType) {
			case env:
				return new DefaultKubernetesClient();
			case kubeconf:
				String userHome = System.getProperty("user.home");
				File confFile = new File(new File(userHome, ".kube"), "config");
				try (InputStream is = new FileInputStream(confFile)) {
					return DefaultKubernetesClient.fromConfig(is);
				}
		}
		throw new RuntimeException("Unsupported config type " + configType);
	}
}
