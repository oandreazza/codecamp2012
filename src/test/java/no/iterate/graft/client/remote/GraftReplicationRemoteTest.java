package no.iterate.graft.client.remote;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import org.junit.Test;

public class GraftReplicationRemoteTest {

	@Test
	public void clientConnectsToSocket() throws IOException, InterruptedException {
		GraftServer server = GraftServer.start(3456);
		try {
			Thread.sleep(10);
			GraftClient client = new GraftClient("localhost", 3456);
			String response = client.ping();
			assertEquals("OK", response);
		} finally {
			server.die();
		}
	}

	@Test(expected=SocketTimeoutException.class)
	public void whatTheHellDoesSOTimeoutDo() throws IOException {
		ServerSocket s = new ServerSocket(12334);
		s.setSoTimeout(1);
		s.accept();
	}

}
