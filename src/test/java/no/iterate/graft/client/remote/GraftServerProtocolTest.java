package no.iterate.graft.client.remote;

import static org.junit.Assert.assertEquals;

import no.iterate.graft.client.remote.GraftServerProtocol;

import org.junit.Before;
import org.junit.Test;


public class GraftServerProtocolTest {
	
	private GraftServerProtocol protocol;
	
	@Before
	public void setUp() {
		protocol = new GraftServerProtocol();
	}
	
	@Test
	public void createAndRetrieveNode() throws Exception {
		String nodeId = protocol.process("createNode");
		
		String serializedNode = protocol.process("getNodeById " + nodeId);
		
		assertEquals("id=" + nodeId, serializedNode);
	}

}
