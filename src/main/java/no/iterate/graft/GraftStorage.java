package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;

public class GraftStorage {
	final Collection<Node> nodes = new ArrayList<Node>();
	final Collection<Edge> edges = new ArrayList<Edge>();
	long nextId = 0;

	public Node getNodeByProperty(String property, String value) {
		for (Node each : nodes) {
			if (value.equals(each.get(property))) {
				return each;
			}
		}
		throw new IllegalStateException("Node not found - property: "
				+ property + " val : " + value);
	}

	public Collection<Edge> getEdgesFrom(String nodeId) {
		Collection<Edge> results = new ArrayList<Edge>();
		for (Edge each : edges) {
			if (each.getFrom().getId().equals(nodeId))
				results.add(each);
		}
		return results;
	}

	Edge getEdgeByProperty(String property, String value) {
		for (Edge each : edges) {
			if (value.equals(each.get(property))) {
				return each;
			}
		}
		throw new IllegalStateException("Edge not found - property: "
				+ property + " val : " + value);
	}

	String generateId() {
		return String.valueOf(nextId++);
	}

	Edge addEdge(Graft listener, Node from, Node to) {
		return addEdgeWithId(generateId(), listener, from, to);
	}

	Edge addEdgeWithId(String id,Graft listener, Node from, Node to) {
		Edge edge = new Edge(id, listener, from, to);
		edges.add(edge);
		return edge;
	}

	Node getNodeById(String nodeId) {
		if (nodeId == null)
			throw new IllegalArgumentException("id required");
		return getNodeByProperty(Graft.ID, nodeId);
	}

	Edge getEdgeById(String nodeId) {
		if (nodeId == null)
			throw new IllegalArgumentException("id required");
		return getEdgeByProperty(Graft.ID, nodeId);
	}

	Node addNode(Graft graft) {
		Node node = new Node(generateId(), graft);
		nodes.add(node);
		return node;
	}
}