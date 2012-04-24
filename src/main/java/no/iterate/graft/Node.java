package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;

public class Node extends PropertiesHolder {

	private final Collection<Edge> edges = new ArrayList<Edge>();

	public Node(String id, NodeListener graft) {
		super(id);
		addListener(graft);
	}

	public Collection<Edge> getEdges() {
		return edges;
	}

	void addEdge(Edge edge) {
		edges.add(edge);
	}

}
