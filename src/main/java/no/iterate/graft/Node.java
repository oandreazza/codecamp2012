package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Node extends PropertiesHolder {

	private final Collection<Edge> edges = new ArrayList<Edge>();

	public Node(String id, NodeListener graft) {
		put("id", id);
		addListener(graft);
	}

	void addEdge(Edge edge) {
		edges.add(edge);
	}

	public String getId() {
		return get("id");
	}

	public Collection<Edge> getEdges() {
		return edges;
	}

	void setProperties(Map<String, String> properties) {
		this.properties.clear();
		this.properties.putAll(properties);
	}
}
