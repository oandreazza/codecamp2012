package no.iterate.geekolympics;

import java.util.ArrayList;
import java.util.Collection;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.Node;

public class GeekOlympics {

	private static final String ID = "id";
	private static final String COMMENT = "comment";
	
	private final Graft db = new Graft();
	private final Node geekUser = new Node("someRandomUser", db);

	public Event createEvent(String id) {
		Node node = db.createNode();
		node.put(ID, id);
		
		return new Event();
	}

	public Event getEventById(String id) {
		return new Event();
	}

	public void addComment(String eventId, String message) {
		Node node = db.getNodeByProperty(ID, eventId);
		Edge comment = db.createEdge(node, geekUser);
		comment.put(COMMENT, message);
	}

	public Collection<String> getComments(String eventId) {
		Collection<Edge> comments = db.getEdgesFrom(eventId);
		Collection<String> commentMessages = new ArrayList<String>(comments.size());

		for (Edge edge : comments) {
			commentMessages.add(edge.get(COMMENT));
		}

		return commentMessages;
	}

}
