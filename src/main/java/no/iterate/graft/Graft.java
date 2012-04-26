package no.iterate.graft;

import no.iterate.graft.client.remote.RemoteReplicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Graft implements NodeListener {

	static final String ID = "id";

	private final GraftStorage graftStorage = new GraftStorage();
	private final GraftSubscriptions graftSubscriptions = new GraftSubscriptions();
	private GraftReplicator graftReplicator = new LocalGraftReplicator();

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

	void applyPropagatedEdge(Edge edge) {
		graftStorage.addReplicaEdge(edge, this);
	}

	public void applyPropagatedNode(Node node) {
		graftStorage.addReplicaNode(node, this);
	}

	void applyPropagatedProperties(Map<String, String> properties) {
		graftStorage.updateNode(properties);
	}

	@Override
	public void update(PropertiesHolder target) {
		graftReplicator.propagateProperties(target);
	}

	public void addReplica(Graft graft) {
		graftReplicator.addReplica(graft);
	}

	// ########################################################################## SUBSCRIPTIONS

	public void subscribe(Node node, IGraftSubscriber subscriber) {
		graftSubscriptions.subscribe(node, subscriber);
	}

	public void notifySubscribers(Edge target) {
		graftSubscriptions.notifySubscribers(target);
	}

	public void setReplicator(GraftReplicator replicator) {
		graftReplicator = replicator;
	}
}
