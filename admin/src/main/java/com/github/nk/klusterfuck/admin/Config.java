package com.github.nk.klusterfuck.admin;

/**
 * Created by nk on 12/6/17.
 */
public class Config {

	KubeConfigType kubeConfigType;
	Env env;

	String flowImage;
	String agentImage;
	String namespace;

	String gogsUrl;
	String gogsUser;
	String gogsPassword;

	public static Config fromEnv() throws Exception {
		Config config = new Config();
		config.env = Env.valueOf(getEnv("DEPLOY_ENV", "prod"));
		config.kubeConfigType = KubeConfigType.valueOf(getEnv("KUBE_CONF_TYPE", "env"));
		config.flowImage = getEnv("FLOW_IMAGE");
		config.agentImage = getEnv("AGENT_IMAGE");
		config.namespace = getEnv("NAMESPACE");
		config.gogsUrl = getEnv("GOGS_URL");
		config.gogsUser = getEnv("GOGS_USER");
		config.gogsPassword = getEnv("GOGS_PASSWORD");
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
