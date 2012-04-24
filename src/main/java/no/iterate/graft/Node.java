package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;

public class Node extends PropertiesHolder {

	private final Collection<Edge> edges = new ArrayList<Edge>();

	void addEdge(Edge edge) {
		edges.add(edge);
	}

	public String getId() {
		return get("id");
	}

	public Collection<Edge> getEdges() {
		return edges;
	}

}
