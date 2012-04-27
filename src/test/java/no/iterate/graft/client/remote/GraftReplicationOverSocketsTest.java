package no.iterate.graft.client.remote;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraftReplicationOverSocketsTest {

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

	@Test
	public void replicateNodeOverSockets() throws Exception {
		Node original = krzys.createNode();
		Node replicated = stig.getNodeByProperty("id", original.getId());
		assertEquals(original.getId(), replicated.getId());
	}
	
	@Test
	public void replicateEdgeOverSockets() {
		Node from = krzys.createNode();
		Node to = krzys.createNode();
		Edge edge = krzys.createEdge(from, to);
		Collection<Edge> edgesFrom = stig.getEdgesFrom(from.getId());
		assertEquals(edge.getId(), edgesFrom.iterator().next().getId());
	}
	
	@Test
	public void replicateNodePropertiesOverSockets() {
		Node node = krzys.createNode();
		node.put("key", "value");
		Node replicated = stig.getNodeByProperty("id", node.getId());
		assertEquals("value", replicated.get("key"));
	}
	
	@Test
	public void replicateEdgePropertiesOverSockets() {
		Node from = krzys.createNode();
		Node to = krzys.createNode();
		Edge edge = krzys.createEdge(from, to);
		edge.put("key", "value");
		Collection<Edge> edgesFrom = stig.getEdgesFrom(from.getId());
		assertEquals("value", edgesFrom.iterator().next().get("key"));
	}
}
