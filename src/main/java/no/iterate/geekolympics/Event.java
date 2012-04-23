package no.iterate.geekolympics;

import java.util.Arrays;
import java.util.Collection;

import no.iterate.graft.Node;

public class Event {
	private final Node node;

	public Event(Node node) {
		this.node = node;
	}

	public void addComment(String message) {
		node.put("comment", message);
	}

	public Collection getComments() {
		return Arrays.asList(node.get("comment"));
	}

}
