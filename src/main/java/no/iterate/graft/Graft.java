package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Graft implements NodeListener {
	private final List<Graft> replicas = new ArrayList<Graft>();

	private final Collection<Node> nodes = new ArrayList<Node>();
	private long nextId = 0;

	public synchronized PropertiesHolder createNode() {
		Node node = new Node(String.valueOf(nextId++), this);
		nodes.add(node);

		addNodeToReplicas(node);

		return node;
	}

	private void addNodeToReplicas(Node node) {
		for (Graft replica : replicas) {
			replica.addNewReplicatedNode(node.getId());
		}
	}

	private void addNewReplicatedNode(String id) {
		nodes.add(new Node(id, this));
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

	public Edge addEdge(Node node1, PropertiesHolder node2) {
		Edge edge = new Edge();
		node1.addEdge(edge);
		return edge;

	}

	public Collection<Edge> getEdgesFrom(String nodeId) {
		Node node = getNodeByProperty("id", nodeId);
		return node.getEdges();
	}

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

	private void addReplica(Graft graft) {
		replicas.add(graft);
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

	private void updateNode(Map<String, String> properties) {
		Node node = getNodeByProperty("id", properties.get("id"));

		node.setProperties(properties);
	}
}
