package no.iterate.geekolympics;

import java.util.ArrayList;
import java.util.Collection;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.Node;

public class GeekOlympics {

	private final Graft db = new Graft();
	private final Node geekUser = new Node("someRandomUser", db);

	public Event createEvent(String id) {
		Node node = db.createNode();
		node.put("id", id);
		return new Event(node);
	}

	public Event getEventById(String id) {
		return new Event(db.getNodeByProperty("id", id));
	}

	public void addComment(String eventId, String message) {
		Node node = db.getNodeByProperty("id", eventId);
		Edge comment = db.addEdge(node, geekUser);
		comment.put("comment", message);
	}

	public Collection<String> getComments(String eventId) {
		Collection<Edge> comments = db.getEdgesFrom(eventId);
		Collection<String> commentMessages = new ArrayList<String>(comments.size());

		for (Edge edge : comments) {
			commentMessages.add(edge.get("comment"));
		}

		return commentMessages;
	}

}
