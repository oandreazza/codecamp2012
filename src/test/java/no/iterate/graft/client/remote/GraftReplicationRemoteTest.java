package no.iterate.graft.client.remote;

import static org.junit.Assert.*;
import no.iterate.graft.Node;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

public class GraftReplicationRemoteTest {

	@Test
	public void replicateDataOverSocket() throws Exception {
		GraftServer first = GraftServer.start(1234);
		GraftServer second = GraftServer.start(1235);
		first.addReplica(second);

		GraftClient client = new GraftClient();
		client.connectTo(1234);
		Node created = client.createNode();
		client.kill();
		client.connectTo(1235);
		Node fetched = client.getNodeById(created.getId());
		assertEquals(created.get("id"), fetched.get("id"));
		
		// kill the second server
		client.kill();
	}
	
	@Ignore
	@Test
	public void setReplicaByCommand() throws Exception {
		GraftClient client = new GraftClient();
		GraftServer first = GraftServer.start(1234);
		GraftServer second = GraftServer.start(1235);
		client.connectTo(1234);
		client.setReplica(1235);
		Node created = client.createNode();
		client.kill();
		client.connectTo(1235);
		Node fetched = client.getNodeById(created.getId());
		assertEquals(created.get("id"), fetched.get("id"));
	}

	@Test
	public void clientConnectsToSocket() throws IOException, InterruptedException {
		GraftServer server = new GraftServer();
		server.invoke();
		try {
			Thread.sleep(10);
			GraftClient client = new GraftClient();
			client.connectTo(3456);
			String response = client.ping();
			assertEquals("OK", response);
		} finally {
			server.die();
		}
	}

	@Test
	public void forceServerToRunALoop() throws IOException, InterruptedException {
		GraftServer graftServer = new GraftServer();
		graftServer.invoke();
		try {
			Thread.sleep(10);
			GraftClient client = new GraftClient();
			client.connectTo(3456);
			String response = client.ping();
			assertEquals("OK", response);
			String second = client.ping();
			assertEquals("OK", second);
		} finally {
			graftServer.die();
		}
	}

	@Test(expected=SocketTimeoutException.class)
	public void whatTheHellDoesSOTimeoutDo() throws IOException {
		ServerSocket s = new ServerSocket(12334);
		s.setSoTimeout(1);
		s.accept();
	}

}
