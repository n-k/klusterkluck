package com.github.nk.klusterfuck.common.dag;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by nipunkumar on 20/05/17.
 */
public class DAG<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Node<T>[] nodes = new Node[0];
    private Link[] links = new Link[0];

	public Node<T>[] getNodes() {
		return nodes;
	}

	public Link[] getLinks() {
		return links;
	}

	public Node<T> getNode(String id) {
        return Arrays.stream(nodes)
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElseGet(null);
    }

    public void addNode(Node<T> node) throws DAGViolationException {
        if (node == null || node.getId() == null || getNodeById(node.getId()) != null) {
            throw new DAGViolationException();
        }
	    List<Node<T>> list = new ArrayList<>();
        list.addAll(Arrays.asList(nodes));
        list.add(node);
	    nodes = list.toArray(new Node[0]);
    }

    public void addLink(Link link) throws DAGViolationException {
        if (link == null || link.getFrom() == null || link.getTo() == null) {
            throw new DAGViolationException();
        }
        if (!hasNode(link.getFrom())) {
            throw new DAGViolationException();
        }
        if (!hasNode(link.getTo())) {
            throw new DAGViolationException();
        }
        if (hasLink(link.getFrom(), link.getTo())) {
            throw new DAGViolationException();
        }
        List<Link> list = new ArrayList<>();
        list.addAll(Arrays.asList(links));
        list.add(link);
        links = list.toArray(new Link[0]);
    }

    public List<Node<T>> getIncomingNodes(String nodeId) {
        List<Node<T>> inNodes = new ArrayList<>();
        for (Link l : links) {
            if (l.getTo().equals(nodeId)) {
                inNodes.add(getNodeById(l.getFrom()));
            }
        }
        return inNodes;
    }

    public List<Node<T>> getOutgoingNodes(String nodeId) {
        List<Node<T>> outNodes = new ArrayList<>();
        for (Link l : links) {
            if (l.getFrom().equals(nodeId)) {
                outNodes.add(getNodeById(l.getTo()));
            }
        }
        return outNodes;
    }

    public <R extends Serializable> DAG<R> map(Function<T, R> fn) {
        DAG<R> newDag = new DAG<R>();
        List<Link> linksClone = new ArrayList<>();
        for (Link l : links) {
            linksClone.add(l.clone());
        }
        newDag.links = linksClone.toArray(new Link[0]);
        List<Node<R>> newNodes = new ArrayList<>();
        for (Node<T> n : nodes) {
            Node<R> newNode = new Node<R>();
            newNode.setId(n.getId());
            newNode.setData(fn.apply(n.getData()));
            newNodes.add(newNode);
        }
        newDag.nodes = newNodes.toArray(new Node[0]);
        return newDag;
    }

    private Node<T> getNodeById(String id) {
        for (Node<T> n : nodes) {
            if (n.getId().equals(id)) {
                return n;
            }
        }
        return null;
    }

    private boolean hasNode(String id) {
        return getNodeById(id) != null;
    }

    private boolean hasLink(String from, String to) {
        for (Link l : links) {
            if (l.getFrom().equals(from) && l.getTo().equals(to)) {
                return true;
            }
        }
        return false;
    }

    private void removeNode(String id) {
        List<Node<T>> newNodes = new ArrayList<>();
        for (Node<T> n : nodes) {
            if (!n.getId().equals(id)) {
                newNodes.add(n);
            }
        }
        nodes = newNodes.toArray(new Node[0]);
        List<Link> newLinks = new ArrayList<>();
        for (Link l : links) {
            if (!(l.getFrom().equals(id) || l.getTo().equals(id))) {
                newLinks.add(l);
            }
        }
        links = newLinks.toArray(new Link[0]);
    }

    private List<Node<T>> getZeroOrderNodes() {
        List<Node<T>> zns = new ArrayList<>();
        for (Node<T> n : nodes) {
            if (getInOrder(n.getId()) == 0) {
                zns.add(n);
            }
        }
        return zns;
    }

    private int getInOrder(String nodeId) {
        int count = 0;
        for (Link l : links) {
            if (l.getTo().equals(nodeId)) {
                count++;
            }
        }
        return count;
    }

    public DAG<T> clone() {
        DAG<T> newDag = new DAG<T>();
        List<Node<T>> clonedNodes = new ArrayList<>();
        for (Node<T> n : nodes) {
            clonedNodes.add(n.clone());
        }
        newDag.nodes = clonedNodes.toArray(new Node[0]);
        List<Link> clonedLinks = new ArrayList<>();
        for (Link l : links) {
            clonedLinks.add(l.clone());
        }
        newDag.links = clonedLinks.toArray(new Link[0]);
        return newDag;
    }

    @JsonIgnore
    public List<Node<T>> getTopologicalOrdering() throws DAGViolationException {
        DAG<T> clone = clone();
        List<Node<T>> ordering = new ArrayList<Node<T>>();
        while (!(clone.nodes == null || clone.nodes.length == 0)) {
            List<Node<T>> zeroOrderNodes = clone.getZeroOrderNodes();
            if (zeroOrderNodes.size() == 0) {
                // cycle!!!
                throw new DAGViolationException();
            }
            for (Node<T> zn : zeroOrderNodes) {
                clone.removeNode(zn.getId());
                ordering.add(zn);
            }
        }
        return ordering;
    }

}
