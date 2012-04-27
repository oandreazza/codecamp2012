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
		kentReplicator.setReplica("localhost", 1235);
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
		Node original = kent.createNode();
		Node replicated = jakub.getNodeByProperty("id", original.getId());
		assertEquals(original.getId(), replicated.getId());
	}
	
	@Test
	public void replicateEdgeOverSockets() {
		Node from = kent.createNode();
		Node to = kent.createNode();
		Edge edge = kent.createEdge(from, to);
		Collection<Edge> edgesFrom = jakub.getEdgesFrom(from.getId());
		assertEquals(edge.getId(), edgesFrom.iterator().next().getId());
	}
	
	@Test
	public void replicateNodePropertiesOverSockets() {
		Node node = kent.createNode();
		node.put("key", "value");
		Node replicated = jakub.getNodeByProperty("id", node.getId());
		assertEquals("value", replicated.get("key"));
	}
	
	@Test
	public void replicateEdgePropertiesOverSockets() {
		Node from = kent.createNode();
		Node to = kent.createNode();
		Edge edge = kent.createEdge(from, to);
		edge.put("key", "value");
		Collection<Edge> edgesFrom = jakub.getEdgesFrom(from.getId());
		assertEquals("value", edgesFrom.iterator().next().get("key"));
	}
}
