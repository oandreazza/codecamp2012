package no.iterate.graft.client.remote;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SetReplicaCommandOverSocketsTest {

	private RemoteReplicator krzysReplicator;
	private RemoteReplicator stigReplicator;
	private Graft krzys;
	private Graft stig;

	@Before
	public void startServers() {
		krzysReplicator = null;
		stigReplicator = null;
		krzys = new Graft();
		stig = new Graft();
	
		krzysReplicator = new RemoteReplicator(1234, krzys);
		krzys.setReplicator(krzysReplicator);
	
		stigReplicator = new RemoteReplicator(1235, stig);
		stig.setReplicator(stigReplicator);
	}

	@After
	public void stopServers() {
		krzysReplicator.die();
		stigReplicator.die();
	}

	@Test
	public void replicateNodeOverSockets() throws Exception {
		// Use Client to tell the server who its replica is
		new GraftClient(1234).setReplica(1235); krzysReplicator.setReplica(1235); // replace with client call
		
		Node original = krzys.createNode();
		Node replicated = stig.getNodeByProperty("id", original.getId());
		
		assertEquals(original.getId(), replicated.getId());
	}
	
}
