package no.iterate.geekolympics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.IGraftSubscriber;
import no.iterate.graft.Node;
import no.iterate.graft.PropertiesHolder;

public class GeekOlympics implements IGraftSubscriber {

	private static final String ID = "id";
	private static final String COMMENT = "comment";

	private Graft db = new Graft();
	private Node geekUser = new Node("someRandomUser", db);
	private final Collection<String> notifications = new LinkedList<String>();

	public GeekOlympics(Graft db) {
		this.db = db;
	}

	public GeekOlympics() {
		this(new Graft());
	}

	public Event createEvent(String id) {
		PropertiesHolder node = db.createNode();
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
		db.notifySubscribers(comment);
		db.subscribe(node, this);
	}

	public Collection<String> getComments(String eventId) {
		Collection<Edge> comments = db.getEdgesFrom(eventId);
		Collection<String> commentMessages = new ArrayList<String>(
				comments.size());

		for (Edge edge : comments) {
			commentMessages.add(edge.get(COMMENT));
		}

		return commentMessages;
	}

	public void login(String username) {
		geekUser = new Node(username, db);
		//todo: lookup user with given name
	}

	public void notify(Edge target) {
		notifications.add(target.getTo().getId() + ": commented on " + target.getFrom().getId() + " " + target.get("comment"));
	}

	public Collection<String> getNotifications() {
		return notifications;
	}

}
