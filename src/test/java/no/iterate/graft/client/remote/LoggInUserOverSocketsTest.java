package no.iterate.graft.client.remote;

import static org.junit.Assert.assertEquals;
import no.iterate.graft.Graft;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoggInUserOverSocketsTest {

	private GraftServer server;

	@Before
	public void startServer() {
		server = GraftServer.start(1234, new Graft());
	}

	@After
	public void killServer() {
		server.die();
	}

	@Test
	public void loggInUser() throws Exception {
		GraftClient jakub = new GraftClient("localhost", 1234);
		GraftClient kent = new GraftClient("localhost", 1234);
		// kent logged in to server
		kent.logIn("Kent");
		// jakub logged in to server
		jakub.logIn("Jakub");
		// jakub post a comment about kent
		jakub.postComment("Some Comment");
		// kent get notified
		assertEquals(1, kent.getNotifications().size());
	}

}
