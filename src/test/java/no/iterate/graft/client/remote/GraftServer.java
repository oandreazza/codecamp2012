package no.iterate.graft.client.remote;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;

public class GraftServer {
	
	private Graft graft = new Graft();
	
	public static GraftServer start(int port) {
		return new GraftServer();
	}

	public void addReplica(GraftServer replica) {
		graft.addReplica(replica.graft);
	}

	public Node createNode() {
		return graft.createNode();
	}

	public Node getNodeById(String id) {
		return graft.getNodeByProperty("id", id);
	}

}
