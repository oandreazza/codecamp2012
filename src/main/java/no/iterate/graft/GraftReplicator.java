package no.iterate.graft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Propagate changes to all replicas (likely over some remoting protocol)
 */
public class GraftReplicator {

	final List<Graft> replicas = new ArrayList<Graft>();

	void addReplica(Graft graft) {
		replicas.add(graft);
	}

	void propagateEdge(Edge edge) {
		for (Graft replica : replicas) {
			replica.applyPropagatedEdge(edge);
		}
	}

	void propagateNode(Node node) {
		for (Graft replica : replicas) {
			replica.applyPropagatedNode(node);
		}
	}

	void propagateProperties(PropertiesHolder target) {
		Map<String, String> properties = target.getProperties();
		for (Graft replica : replicas)
			replica.applyPropagatedProperties(properties);
	}

}
