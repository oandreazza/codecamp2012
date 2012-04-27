package no.iterate.graft.client.remote;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Collection;

import static org.junit.Assert.*;

public class GraftReplicationOverSocketsTest {

	private RemoteReplicator krzysReplicator;
	private RemoteReplicator stigReplicator;
	private Graft krzys;
	private Graft stig;

	@Test
	public void replicateOverSockets() throws Exception {
		Node original = krzys.createNode();
		Node replicated = stig.getNodeByProperty("id", original.getId());
		assertEquals(original.getId(), replicated.getId());
	}
	
	@Test
	public void replicateEdgesToo() {
		Node from = krzys.createNode();
		Node to = krzys.createNode();
		Edge edge = krzys.createEdge(from, to);
		Collection<Edge> edgesFrom = stig.getEdgesFrom(from.getId());
		assertEquals(edge.getId(), edgesFrom.iterator().next().getId());
	}

	@Before
	public void startServers() {
		krzysReplicator = null;
		stigReplicator = null;
		krzys = new Graft();
		stig = new Graft();

		krzysReplicator = new RemoteReplicator(1234, 1235, krzys);
		krzys.setReplicator(krzysReplicator);

		stigReplicator = new RemoteReplicator(1235, 1234, stig);
		stig.setReplicator(stigReplicator);
	}

	@After
	public void stopServers() {
		krzysReplicator.die();
		stigReplicator.die();
	}
}
