package no.iterate.graft;

import java.util.*;

import no.iterate.geekolympics.GeekOlympics;

public class Graft implements NodeListener {

	static final String ID = "id";
	private final List<Graft> replicas = new ArrayList<Graft>();

	private Map<String, Collection<GeekOlympics>> subscriptions = new HashMap<String, Collection<GeekOlympics>>();
	private final GraftStorage graftStorage = new GraftStorage();

	public static List<Graft> getTwoGrafts() {
		List<Graft> grafts = new ArrayList<Graft>();
		Graft graft1 = new Graft();
		Graft graft2 = new Graft();
		graft1.addReplica(graft2);
		graft2.addReplica(graft1);
		grafts.add(graft1);
		grafts.add(graft2);

		return grafts;
	}

	public synchronized Node createNode() {
		Node node = graftStorage.addNode(this);
		addNodeToReplicas(node);

		return node;
	}

	public Node getNodeByProperty(String property, String value) {
		return graftStorage.getNodeByProperty(property, value);
	}

	public Edge createEdge(Node from, Node to) {
		Edge edge = graftStorage.addEdge(this, from, to);
		for (Graft replica : replicas) {
			replica.addReplicaEdge(edge.getId(), from.getId(), to.getId());
		}
		return edge;
	}

	public Collection<Edge> getEdgesFrom(String nodeId) {
		return graftStorage.getEdgesFrom(nodeId);
	}

	public void kill() {
		graftStorage.nodes.clear();
	}

	public void update(PropertiesHolder target) {
		Map<String, String> properties = target.getProperties();
		for (Graft each : replicas) {
			each.updateNode(properties);
		}
	}

	public void subscribe(String eventId, GeekOlympics geekOlympics) {
		Collection<GeekOlympics> gekGeekOlympics = subscriptions.get(eventId);
		if (gekGeekOlympics == null) {
			gekGeekOlympics = new HashSet<GeekOlympics> ();
			subscriptions.put(eventId, gekGeekOlympics);
		}
		gekGeekOlympics.add(geekOlympics);
	}

	public void notifySubscribers(String eventId, String message,
			String userName) {
		Collection<GeekOlympics> collection = subscriptions.get(eventId);
		if (collection == null) {
			return; // Never mind...
		}

		String eventName = graftStorage.getNodeById(eventId).get(ID);
		for (GeekOlympics each : collection) {
			each.notifyComment(message, eventName, userName);
		}
	}

	private void addReplicaEdge(String edgeId, String fromId, String toId) {
		Node from = graftStorage.getNodeById(fromId);
		Node to = graftStorage.getNodeById(toId);
		graftStorage.addEdgeWithId(edgeId, this, from, to);
	}

	private void addReplica(Graft graft) {
		replicas.add(graft);
	}

	private void updateNode(Map<String, String> properties) {
		String targetId = properties.get(ID);
		PropertiesHolder node;
		try {
			node = graftStorage.getNodeById(targetId);
		} catch (IllegalStateException e) {
			node = graftStorage.getEdgeById(targetId);
		}
		node.setProperties(properties);

	}

	private void addNodeToReplicas(PropertiesHolder node) {
		for (Graft replica : replicas) {
			replica.addNewReplicatedNode(node.getId());
		}
	}

	private void addNewReplicatedNode(String id) {
		graftStorage.nodes.add(new Node(id, this));
	}
}
