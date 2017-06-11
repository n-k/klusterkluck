package com.github.nk.klusterfuck.common;

/**
 * Created by nk on 9/6/17.
 */
public class ConnectorRef extends StepRef {
	private String connectorId;
	public ConnectorRef() {
		super(RefType.connector);
	}

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}
}
