package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Graft implements NodeListener {
	private final List<Graft> replicas = new ArrayList<Graft>();

	private final Collection<Node> nodes = new ArrayList<Node>();
	private long nextId = 0;

	public synchronized Node createNode() {
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
		return null;
	}

	public Edge addEdge(Node node1, Node node2) {
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

	public void update() {
		
	}
}
