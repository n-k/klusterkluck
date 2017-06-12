package com.github.nk.klusterfuck.agent;

import static spark.Spark.*;

public class Application {

	public static void main(String[] args) throws Exception {
		Config config = getconfig();
		FunctionController ctrl = new FunctionController(config);
		ctrl.init();

		port(5000);

		get("/", (req, res) -> ctrl.getFunctionConfig());

		post("/", (req, res) -> {
			String body = req.body();
			return ctrl.run(body);
		});
	}

	private static Config getconfig() {
		Config config = new Config();
		config.setWorkDir(getEnv("WORK_DIR"));
		config.setGitUrl(getEnv("GIT_URL"));
		config.setGogsUser(getEnv("GOGS_USER"));
		config.setGogsPassword(getEnv("GOGS_PASSWORD"));
		config.setCommitId(getEnv("GIT_COMMIT", ""));
		config.setDisableCheckout(Boolean.valueOf(getEnv("DISABLE_CHECKOUT", "false")));
		config.setDisableCache(Boolean.valueOf(getEnv("DISABLE_CACHE", "false")));
		return config;
	}

	private static String getEnv(String name, String defaultVal) {
		String env = System.getenv(name);
		if (env == null || env.isEmpty()) {
			return defaultVal;
		}
		return env;
	}

	private static String getEnv(String name) {
		String env = System.getenv(name);
		if (env == null) {
			throw new RuntimeException("No value for required env var " + name);
		}
		return env;
	}
}
