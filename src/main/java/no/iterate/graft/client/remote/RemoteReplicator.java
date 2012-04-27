package no.iterate.graft.client.remote;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.GraftReplicator;
import no.iterate.graft.Node;
import no.iterate.graft.PropertiesHolder;

public class RemoteReplicator implements GraftReplicator {

	private GraftClient replicaClient;
	private final GraftServer server;

	public RemoteReplicator(int port, Graft db) {
		server = GraftServer.start(port, db);
	}
	
	@Override
	public void setReplica(String hostname, int port) {
		replicaClient = new GraftClient(hostname, port);
	}

	@Override
	public void propagateEdge(Edge edge) {
		replicaClient.propagateEdge(edge);
	}

	@Override
	public void propagateNode(Node node) {
		replicaClient.propagateNode(node);
	}

	@Override
	public void propagateProperties(PropertiesHolder target) {
		replicaClient.propagateProperties(target);
	}

	@Override
	public void die() {
		server.die();
	}
}
