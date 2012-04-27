package no.iterate.graft;

public interface GraftReplicator {

	void propagateEdge(Edge edge);

	void propagateNode(Node node);

	void propagateProperties(PropertiesHolder target);

	void die();

	void setReplica(String hostname, int port);
}
