package com.github.nk.klusterfuck.common;

/**
 * Created by nk on 5/6/17.
 */
public class DAG {

	private Node[] nodes;
	private Link[] links;

	public Node[] getNodes() {
		return nodes;
	}

	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

	public Link[] getLinks() {
		return links;
	}

	public void setLinks(Link[] links) {
		this.links = links;
	}
}
