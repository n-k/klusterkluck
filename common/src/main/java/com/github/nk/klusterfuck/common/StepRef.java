package com.github.nk.klusterfuck.common;

/**
 * Created by nk on 6/6/17.
 */
public class StepRef {
	public enum RefType {function, connector}

	private String url;
	private RefType type;

	public StepRef() {}

	public StepRef(RefType type, String url) {
		this.type = type;
		this.url = url;
	}

	public RefType getType() {
		return type;
	}

	public void setType(RefType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
