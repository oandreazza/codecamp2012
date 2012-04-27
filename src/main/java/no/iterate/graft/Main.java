package no.iterate.graft;

import no.iterate.graft.client.remote.RemoteReplicator;

import java.util.Date;

public class Main {
	public static void main(String[] args) {
		int port = 33333;
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}
		System.out.format("Server starting at %s\n", new Date());
		Graft graft = new Graft();
		RemoteReplicator replicator = new RemoteReplicator(port, graft);
		graft.setReplicator(replicator);
	}
}
