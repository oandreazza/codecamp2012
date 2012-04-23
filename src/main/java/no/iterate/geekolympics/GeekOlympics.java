package no.iterate.geekolympics;

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

}
