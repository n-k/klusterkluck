package com.github.nk.klusterfuck.agent;

import com.github.nk.klusterfuck.common.FunctionConfig;

import static spark.Spark.*;

public class Application {

	public static void main(String[] args) throws Exception {
		AgentConfig agentConfig = getconfig();
		if (!agentConfig.isDisableCheckout()) {
			SetupUtils.setupClone(
					agentConfig.getWorkDir(),
					agentConfig.getGitUrl(),
					agentConfig.getCommitId(),
					agentConfig.getGitUser(),
					agentConfig.getGitPassword());
		}
		FunctionConfig functionConfig = SetupUtils.readConfig(agentConfig.getWorkDir());
		port(5000);
		new FunctionController(functionConfig, agentConfig);
	}

	private static AgentConfig getconfig() {
		AgentConfig agentConfig = new AgentConfig();
		agentConfig.setWorkDir(getEnv("WORK_DIR"));
		agentConfig.setGitUrl(getEnv("GIT_URL"));
		agentConfig.setGitUser(getEnv("GIT_USER"));
		agentConfig.setGitPassword(getEnv("GIT_PASSWORD"));
		agentConfig.setCommitId(getEnv("GIT_COMMIT", ""));
		agentConfig.setDisableCheckout(Boolean.valueOf(getEnv("DISABLE_CHECKOUT", "false")));
		agentConfig.setDisableCache(Boolean.valueOf(getEnv("DISABLE_CACHE", "false")));
		return agentConfig;
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
