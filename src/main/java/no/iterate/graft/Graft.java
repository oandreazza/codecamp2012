package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Graft implements NodeListener {

	static final String ID = "id";

	private final GraftStorage graftStorage = new GraftStorage();
	private final GraftSubscriptions graftSubscriptions = new GraftSubscriptions();
	private GraftReplicator graftReplicator = new NullGraftReplicator();

	public static List<Graft> getTwoGrafts() {
		List<Graft> grafts = new ArrayList<Graft>();
		Graft graft1 = new Graft();
		Graft graft2 = new Graft();

		final LocalGraftReplicator replicator1 = new LocalGraftReplicator(graft2);
		graft1.setReplicator(replicator1);
		final LocalGraftReplicator replicator2 = new LocalGraftReplicator(graft1);
		graft2.setReplicator(replicator2);

		grafts.add(graft1);
		grafts.add(graft2);

		return grafts;
	}

	public Node createNode() {
		Node node = graftStorage.addNode(this);
		graftReplicator.propagateNode(node);
		return node;
	}

	public Edge createEdge(Node from, Node to) {
		Edge edge = graftStorage.addEdge(this, from, to);
		graftReplicator.propagateEdge(edge);
		return edge;
	}

	// ########################################################################## QUERIES

	public Node getNodeByProperty(String property, String value) {
		return graftStorage.getNodeByProperty(property, value);
	}

	public Collection<Edge> getEdgesFrom(String nodeId) {
		return graftStorage.getEdgesFrom(nodeId);
	}

	public void kill() {
		graftStorage.kill();
	}

	// ########################################################################## REPLICATION


	@Override
	public void update(PropertiesHolder target) {
		graftReplicator.propagateProperties(target);
	}

	public void setReplicator(GraftReplicator replicator) {
		graftReplicator = replicator;
	}

	public void applyPropagatedEdge(Edge edge) {
		graftStorage.addReplicaEdge(edge, this);
	}

	public void applyPropagatedNode(Node node) {
		graftStorage.addReplicaNode(node, this);
	}

	public void applyPropagatedProperties(Map<String, String> properties) {
		graftStorage.updateNode(properties);
	}

	// ########################################################################## SUBSCRIPTIONS

	public void subscribe(Node node, IGraftSubscriber subscriber) {
		graftSubscriptions.subscribe(node, subscriber);
	}

	public void notifySubscribers(Edge target) {
		graftSubscriptions.notifySubscribers(target);
	}

	public void setReplica(String hostname, int port) {
		graftReplicator.setReplica(hostname, port);
	}

}
