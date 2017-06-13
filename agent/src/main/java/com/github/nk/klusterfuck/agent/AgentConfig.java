package com.github.nk.klusterfuck.agent;

/**
 * Created by nk on 12/6/17.
 */
public class AgentConfig {

	//WORK_DIR
	private String workDir;
	//GIT_URL
	private String gitUrl;
	//GOGS_USER
	private String gogsUser;
	//GOGS_PASSWORD
	private String gogsPassword;
	/*
	If not specified, commit id is set to "", and latest commit is used
	 */
	//GIT_COMMIT
	private String commitId;
	//DISABLE_CHECKOUT
	private boolean disableCheckout;
	//DISABLE_CACHE:false
	private boolean disableCache;

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public String getGitUrl() {
		return gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}

	public String getGogsUser() {
		return gogsUser;
	}

	public void setGogsUser(String gogsUser) {
		this.gogsUser = gogsUser;
	}

	public String getGogsPassword() {
		return gogsPassword;
	}

	public void setGogsPassword(String gogsPassword) {
		this.gogsPassword = gogsPassword;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public boolean isDisableCheckout() {
		return disableCheckout;
	}

	public void setDisableCheckout(boolean disableCheckout) {
		this.disableCheckout = disableCheckout;
	}

	public boolean isDisableCache() {
		return disableCache;
	}

	public void setDisableCache(boolean disableCache) {
		this.disableCache = disableCache;
	}
}
