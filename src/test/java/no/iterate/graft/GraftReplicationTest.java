package no.iterate.graft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class GraftReplicationTest {

	@Test
	public void writesAreReplicated() {
		// start up two grafts
		List<Graft> grafts = Graft.getTwoGrafts();
		Graft first = grafts.get(0);
		Graft second = grafts.get(1);
		
		// write some data to first graft
		PropertiesHolder firstGraftNode = first.createNode();
		firstGraftNode.put("key", "value");
		// replicate data to second one in background
		
		// kill the first graft with shotgun
		first.kill();
		
		// check replicated data in second graft
		PropertiesHolder secondGraftNode = second.getNodeByProperty("key", "value");
		assertNotNull(secondGraftNode);
		assertNotSame(firstGraftNode, secondGraftNode);
	}

	@Test
	public void writesOfEdgesAreReplicated() {
		// start up two grafts
		List<Graft> grafts = Graft.getTwoGrafts();
		Graft first = grafts.get(0);
		Graft second = grafts.get(1);

		// write some data to first graft
		Node fromFirst = first.createNode();
		Node to = first.createNode();
		first.createEdge(fromFirst, to);

		first.kill();

		Node fromAtSecond = second.getNodeByProperty("id", fromFirst.getId());
		Collection<Edge> edges = second.getEdgesFrom(fromAtSecond.getId());
		assertEquals("The edge should have been replicated", 1, edges.size());
	}
	
	
	@Test
	public void writesOfEdgesPropertiesAreReplicated() {
		// start up two grafts
		List<Graft> grafts = Graft.getTwoGrafts();
		Graft first = grafts.get(0);
		Graft second = grafts.get(1);

		// write some data to first graft
		Node fromFirst = first.createNode();
		Node to = first.createNode();
		Edge firstEdge = first.createEdge(fromFirst, to);
		firstEdge.put("key", "value");

		first.kill();

		Node fromAtSecond = second.getNodeByProperty("id", fromFirst.getId());
		Collection<Edge> edges = second.getEdgesFrom(fromAtSecond.getId());
		assertEquals("value", edges.iterator().next().get("key"));
	}
}
