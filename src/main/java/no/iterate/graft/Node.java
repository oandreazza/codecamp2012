package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Node extends PropertiesHolder {

	private final Collection<Edge> edges = new ArrayList<Edge>();
	private final Collection<NodeListener> listeners = new ArrayList<NodeListener>();

	public Node(String id, NodeListener graft) {
		addListener(graft);
		put("id", id);
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

	void addListener(NodeListener myListener) {
		listeners.add(myListener);
		myListener.update();
	}

	Collection<NodeListener> getListeners() {
		return listeners;
	}
}
