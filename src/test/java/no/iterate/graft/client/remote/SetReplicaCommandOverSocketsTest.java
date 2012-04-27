package no.iterate.graft.client.remote;

import static org.junit.Assert.assertEquals;
import no.iterate.graft.Graft;
import no.iterate.graft.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SetReplicaCommandOverSocketsTest {

	private RemoteReplicator kentReplicator;
	private RemoteReplicator jakubReplicator;
	private Graft kent;
	private Graft jakub;

	@Before
	public void startServers() {
		kentReplicator = null;
		jakubReplicator = null;
		kent = new Graft();
		jakub = new Graft();
	
		kentReplicator = new RemoteReplicator(1234, kent);
		kent.setReplicator(kentReplicator);
	
		jakubReplicator = new RemoteReplicator(1235, jakub);
		jakub.setReplicator(jakubReplicator);
	}

	@After
	public void stopServers() {
		kentReplicator.die();
		jakubReplicator.die();
	}

	@Test
	public void replicateNodeOverSockets() throws Exception {
		kentReplicator.setReplica("localhost", 1235);
		
		Node original = kent.createNode();
		Node replicated = jakub.getNodeByProperty("id", original.getId());
		
		assertEquals(original.getId(), replicated.getId());
	}
	
}
