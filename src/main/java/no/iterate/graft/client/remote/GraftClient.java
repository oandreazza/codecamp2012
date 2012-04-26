package no.iterate.graft.client.remote;

import no.iterate.graft.Node;

public class GraftClient {

	private GraftServer server;

	public void connectTo(GraftServer first) {
		this.server = first;
	}

	public Node createNode() {
		return server.createNode();
	}

	public void kill() {
		// TODO Auto-generated method stub
	}

	public Node getNodeById(String id) {
		return server.getNodeById(id);
	}

}
