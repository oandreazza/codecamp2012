package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Graft implements NodeListener {
	
	private final List<Graft> replicas = new ArrayList<Graft>();
	private final Collection<Node> nodes = new ArrayList<Node>();
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
		Node node = new Node(String.valueOf(nextId++), this);
		nodes.add(node);

		addNodeToReplicas(node);

		return node;
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

	public Edge createEdge(Node node1, Node node2) {
		Edge edge = addEdge(node1);
		for (Graft replica : replicas) {
			replica.addReplicaEdge(node1.getId(), node2.getId());
		}
		return edge;
	}

	private Edge addEdge(Node node1) {
		Edge edge = new Edge();
		node1.addEdge(edge);
		return edge;
	}

	private void addReplicaEdge(String fromId, String toId) {
		Node from = getNodeById(fromId);
		Node to = getNodeById(toId);
		Edge edge = addEdge(from);
	}

	public Collection<Edge> getEdgesFrom(String nodeId) {
		Node node = getNodeById(nodeId);
		return node.getEdges();
	}

	private Node getNodeById(String nodeId) {
		return getNodeByProperty("id", nodeId);
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
		PropertiesHolder node = getNodeById(properties.get("id"));
		node.setProperties(properties);
	}

	private void addNodeToReplicas(Node node) {
		for (Graft replica : replicas) {
			replica.addNewReplicatedNode(node.getId());
		}
	}

	private void addNewReplicatedNode(String id) {
		nodes.add(new Node(id, this));
	}
}
