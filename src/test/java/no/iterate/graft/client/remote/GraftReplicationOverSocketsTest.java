package no.iterate.graft.client.remote;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class GraftReplicationOverSocketsTest {

	@Test
	public void replicateOverSockets() throws Exception {

		RemoteReplicator krzysReplicator = null;
		RemoteReplicator stigReplicator = null;
		try {
			Graft krzys = new Graft();
			Graft stig = new Graft();

			krzysReplicator = new RemoteReplicator(1234, 1235, krzys);
			krzys.setReplicator(krzysReplicator);

			stigReplicator = new RemoteReplicator(1235, 1234, stig);
			stig.setReplicator(stigReplicator);

			Node original = krzys.createNode();

			Node replicated = stig.getNodeByProperty("id", original.getId());

			assertEquals(original.getId(), replicated.getId());
		} finally {
			krzysReplicator.die();
			stigReplicator.die();
		}
	}
}
