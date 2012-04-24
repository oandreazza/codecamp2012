package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;

public class Graft {

	private final Collection<Node> nodes = new ArrayList<Node>();

	public Node createNode() {
		Node node = new Node();
		nodes.add(node);
		return node;
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

}
