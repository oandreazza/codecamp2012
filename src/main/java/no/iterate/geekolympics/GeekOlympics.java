package no.iterate.geekolympics;

import java.util.Arrays;
import java.util.Collection;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;

public class GeekOlympics {

	private final Graft db = new Graft();

	public Event createEvent(String id) {
		Node node = db.createNode();
		node.put("id", id);
		return new Event(node);
	}

	public Event getEventById(String id) {
		return new Event(db.getNodeByProperty("id", id));
	}

	public void addComment(String eventId, String message) {
		db.getNodeByProperty("id", eventId)
			.put("comment", message);
	}

	public Collection<String> getComments(String eventId) {
		return Arrays.asList(db.getNodeByProperty("id", eventId).get("comment"));
	}

}
