package com.github.nk.klusterfuck.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by nk on 6/6/17.
 */
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
		property = "category")
@JsonSubTypes({
		@JsonSubTypes.Type(value = ConnectorRef.class, name = "connector"),
		@JsonSubTypes.Type(value = FunctionRef.class, name = "fn")
})
abstract public class StepRef {
	public enum RefType {fn, connector}

	private RefType category;
	private String text;

	public StepRef(RefType category) {
		this.category = category;
	}

	public RefType getCategory() {
		return category;
	}

	public void setCategory(RefType category) {
		this.category = category;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
