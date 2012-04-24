package no.iterate.graft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.junit.Test;

public class GraftReplicationTest {

	@Test
	public void shouldReplicateGraft() {
		// start up two grafts
		List<Graft> grafts = Graft.getTwoGrafts();
		Graft first = grafts.get(0);
		Graft second = grafts.get(1);
		
		// write some data to first graft
		Node firstGraftNode = first.createNode();
		firstGraftNode.put("key", "value");
		// replicate data to second one in background
		
		// kill the first graft with shotgun
		first.kill();
		
		// check replicated data in second graft
		Node secondGraftNode = second.getNodeByProperty("key", "value");
		assertNotNull(secondGraftNode);
		assertNotSame(firstGraftNode, secondGraftNode);
	}
}
