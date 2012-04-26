package no.iterate.graft.client.remote;

import static org.junit.Assert.*;
import no.iterate.graft.Node;

import org.junit.Test;

public class GraftReplicationRemoteTest {

	@Test
	public void replicateDataOverSocket() throws Exception {
		GraftClient client = new GraftClient();
		GraftServer first = GraftServer.start(1234);
		GraftServer second = GraftServer.start(1235);
		first.addReplica(second);
		client.connectTo(first);
		Node created = client.createNode();
		client.kill();
		client.connectTo(second);
		Node fetched = client.getNodeById(created.getId());
		assertEquals(created.get("id"), fetched.get("id"));
		
		// kill the second server
		client.kill();
	}
	
	
	@Test
	public void setReplicaByCommand() throws Exception {
		GraftClient client = new GraftClient();
		GraftServer first = GraftServer.start(1234);
		GraftServer second = GraftServer.start(1235);
		client.connectTo(first);
		client.setReplica(1235);
		Node created = client.createNode();
		client.kill();
		client.connectTo(second);
		Node fetched = client.getNodeById(created.getId());
		assertEquals(created.get("id"), fetched.get("id"));
	}

}
