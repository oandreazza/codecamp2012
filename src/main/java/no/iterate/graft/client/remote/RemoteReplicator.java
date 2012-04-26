package no.iterate.graft.client.remote;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.GraftReplicator;
import no.iterate.graft.Node;
import no.iterate.graft.PropertiesHolder;

public class RemoteReplicator implements GraftReplicator {

	private final GraftClient client;
	private final GraftServer server;

	public RemoteReplicator(int port, int remote, Graft db) {
		client = new GraftClient(remote);
		server = GraftServer.start(port, db);
	}

	@Override
	public void propagateEdge(Edge edge) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void propagateNode(Node node) {
		client.propagateNode(node);
	}

	@Override
	public void propagateProperties(PropertiesHolder target) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void die() {
		server.die();
	}
}
