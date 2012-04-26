package no.iterate.graft.client.remote;

import static org.junit.Assert.*;
import no.iterate.graft.Node;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

public class GraftReplicationRemoteTest {

	private GraftServer first = GraftServer.start(1234);
	private GraftServer second = GraftServer.start(1235);
	
	@After
	public void shutDown() {
		first.stop();
		second.stop();
	}

	@Test
	public void replicateDataOverSocket() throws Exception {
		GraftClient client = new GraftClient();
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
	public void serverTalksToGraft() throws Exception {
		// once upon time there was a server
		GraftServer server = new GraftServer();
		// we told it to create a node and it returned it
		Node newNode = server.createNode();
		// we told it to find the node by its id
		Node foundNode = server.getNodeById(newNode.getId());
		// and it worked and we lived happily ever after
		assertEquals(newNode.getId(), foundNode.getId());
		
	}
	
	@Test
	public void clientTalksToServer() throws Exception {
		GraftClient client = new GraftClient();
		client.connectTo(new GraftServer());
		
		Node newNode = client.createNode();
		Node foundNode = client.getNodeById(newNode.getId());
		
		assertEquals(newNode.getId(), foundNode.getId());
	}
	
	@Test
	public void serverReplicatesToTheOtherServer() throws Exception {
		GraftServer first = new GraftServer();
		GraftServer second = new GraftServer();
		first.addReplica(second);

		Node newNode = first.createNode();
		Node foundNode = second.getNodeById(newNode.getId());

		assertEquals(newNode.getId(), foundNode.getId());
	}

	// @Test
	// public void setReplicaByCommand() throws Exception {
	// GraftClient client = new GraftClient();
	// GraftServer first = GraftServer.start(1234);
	// GraftServer second = GraftServer.start(1235);
	// client.connectTo(first);
	// client.setReplica(1235);
	// Node created = client.createNode();
	// client.kill();
	// client.connectTo(second);
	// Node fetched = client.getNodeById(created.getId());
	// assertEquals(created.get("id"), fetched.get("id"));
	// }

}
