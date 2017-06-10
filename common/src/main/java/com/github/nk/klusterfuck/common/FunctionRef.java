package com.github.nk.klusterfuck.common;

/**
 * Created by nk on 9/6/17.
 */
public class FunctionRef extends StepRef {
	private String url;

	public FunctionRef() {
		super(RefType.fn);
	}

	public FunctionRef(String url) {
		super(RefType.fn);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
