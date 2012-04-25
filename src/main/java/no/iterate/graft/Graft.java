package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Graft implements NodeListener {

	static final String ID = "id";
	private final List<Graft> replicas = new ArrayList<Graft>();

	private final GraftStorage graftStorage = new GraftStorage();
	private final GraftSubscriptions graftSubscriptions = new GraftSubscriptions();

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
		propagateNode(node);

		return node;
	}

	public Edge createEdge(Node from, Node to) {
		Edge edge = graftStorage.addEdge(this, from, to);
		propagateEdge(edge);
		return edge;
	}

	public Node getNodeByProperty(String property, String value) {
		return graftStorage.getNodeByProperty(property, value);
	}

	public Collection<Edge> getEdgesFrom(String nodeId) {
		return graftStorage.getEdgesFrom(nodeId);
	}

	public void kill() {
		graftStorage.kill();
	}

	public void subscribe(Node node, IGraftSubscriber subscriber) {
		graftSubscriptions.subscribe(node, subscriber);
	}

	public void notifySubscribers(Edge target) {
		graftSubscriptions.notifySubscribers(target);
	}


	private void propagateEdge(Edge edge) {
		for (Graft replica : replicas) {
			replica.applyPropagatedEdge(edge);
		}
	}

	private void applyPropagatedEdge(Edge edge) {
		graftStorage.addReplicaEdge(edge, this);
	}

	private void propagateNode(Node node) {
		for (Graft replica : replicas) {
			replica.applyPropagatedNode(node);
		}
	}

	private void applyPropagatedNode(Node node) {
		graftStorage.addReplicaNode(node, this);
	}

	public void update(PropertiesHolder target) {
		Map<String, String> properties = target.getProperties();
		for (Graft each : replicas) {
			each.graftStorage.updateNode(properties);
		}
	}

	private void addReplica(Graft graft) {
		replicas.add(graft);
	}

}
