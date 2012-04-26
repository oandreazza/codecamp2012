package no.iterate.graft;

public interface GraftReplicator {
	@Deprecated
	void addReplica(Graft graft);

	void propagateEdge(Edge edge);

	void propagateNode(Node node);

	void propagateProperties(PropertiesHolder target);

	void die();
}
