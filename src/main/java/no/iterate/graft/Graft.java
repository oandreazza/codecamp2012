package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Graft implements NodeListener {
	
	private final List<Graft> replicas = new ArrayList<Graft>();
	private final Collection<Node> nodes = new ArrayList<Node>();
	private final Collection<Edge> edges = new ArrayList<Edge>();
	private long nextId = 0;

	public static List<Graft> getTwoGrafts() {
		List<Graft> grafts = new ArrayList<Graft>();
		Graft graft1 = new Graft();
		Graft graft2 = new Graft();
		graft1.addReplica(graft2);
		graft2.addReplica(graft1);
		grafts.add(graft1);
		grafts.add(graft2);
	
		return grafts;
	}

	public synchronized Node createNode() {
		Node node = new Node(generateId(), this);
		nodes.add(node);

		addNodeToReplicas(node);

		return node;
	}

	private String generateId() {
		return String.valueOf(nextId++);
	}

	public Node getNodeByProperty(String property, String value) {
		for (Node each : nodes) {
			if (value.equals(each.get(property))) {
				return each;
			}
		}
		throw new IllegalStateException("Node not found - property: "
				+ property + " val : " + value);
	}
	
	private Edge getEdgeByProperty(String property, String value) {
		for (Edge each : edges) {
			if (value.equals(each.get(property))) {
				return each;
			}
		}
		throw new IllegalStateException("Edge not found - property: "
				+ property + " val : " + value);
	}

	public Edge createEdge(Node node1, PropertiesHolder node2) {
		Edge edge = addEdge(node1);
		for (Graft replica : replicas) {
			replica.addReplicaEdge(edge.getId(), node1.getId(), node2.getId());
		}
		return edge;
	}

	private Edge addEdge(Node node1) {
		return addEdgeWithId(generateId(), node1);
	}

	private Edge addEdgeWithId(String id, Node node1) {
		Edge edge = new Edge(id, this);
		node1.addEdge(edge);
		edges.add(edge);
		return edge;
	}

	private void addReplicaEdge(String edgeId, String fromId, String toId) {
		Node from = getNodeById(fromId);
		PropertiesHolder to = getNodeById(toId);
		Edge edge = addEdgeWithId(edgeId, from);
	}

	public Collection<Edge> getEdgesFrom(String nodeId) {
		Node node = getNodeById(nodeId);
		return node.getEdges();
	}

	private Node getNodeById(String nodeId) {
		if (nodeId == null) throw new IllegalArgumentException("id required");
		return getNodeByProperty("id", nodeId);
	}

	private Edge getEdgeById(String nodeId) {
		if (nodeId == null) throw new IllegalArgumentException("id required");
		return getEdgeByProperty("id", nodeId);
	}

	public void kill() {
		nodes.clear();
	}

	public void update(PropertiesHolder target) {
		Map<String, String> properties = target.getProperties();
		for (Graft each : replicas) {
			each.updateNode(properties);
		}
	}

	private void addReplica(Graft graft) {
		replicas.add(graft);
	}

	private void updateNode(Map<String, String> properties) {
		String targetId = properties.get("id");
		PropertiesHolder node;
		try {
			node = getNodeById(targetId);
		} catch (IllegalStateException e) {
			node = getEdgeById(targetId);
		}
		node.setProperties(properties);
	}

	private void addNodeToReplicas(PropertiesHolder node) {
		for (Graft replica : replicas) {
			replica.addNewReplicatedNode(node.getId());
		}
	}

	private void addNewReplicatedNode(String id) {
		nodes.add(new Node(id, this));
	}
}
