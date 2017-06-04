package com.github.nk.klusterfuck.admin.services;

/**
 * Created by nk on 4/6/17.
 */
public class RepoInfo {
	private String gitUrl;
	private String commitId;

	public String getGitUrl() {
		return gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
}
