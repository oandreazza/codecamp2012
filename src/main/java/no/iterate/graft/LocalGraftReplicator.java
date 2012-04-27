package no.iterate.graft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Propagate changes to all replicas (likely over some remoting protocol)
 */
public class LocalGraftReplicator implements GraftReplicator {

	final List<Graft> replicas = new ArrayList<Graft>();

	public LocalGraftReplicator(Graft graft2) {
		replicas.add(graft2);
	}

	@Override
	public void propagateEdge(Edge edge) {
		for (Graft replica : replicas) {
			replica.applyPropagatedEdge(edge);
		}
	}

	@Override
	public void propagateNode(Node node) {
		for (Graft replica : replicas) {
			replica.applyPropagatedNode(node);
		}
	}

	@Override
	public void propagateProperties(PropertiesHolder target) {
		Map<String, String> properties = target.getProperties();
		for (Graft replica : replicas)
			replica.applyPropagatedProperties(properties);
	}

	@Override
	public void die() {
		// NO OP here
	}

	@Override
	public void setReplica(String hostname, int port) {
	}

}
