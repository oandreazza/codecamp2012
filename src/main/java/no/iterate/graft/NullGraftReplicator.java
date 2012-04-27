package no.iterate.graft;

public class NullGraftReplicator implements GraftReplicator {

	@Override
	public void propagateEdge(Edge edge) {
	}

	@Override
	public void propagateNode(Node node) {
	}

	@Override
	public void propagateProperties(PropertiesHolder target) {
	}

	@Override
	public void die() {
	}

	@Override
	public void setReplica(String hostname, int port) {
	}
}
