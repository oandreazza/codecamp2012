package no.iterate.graft.client.remote;

import static org.junit.Assert.*;
import no.iterate.graft.Node;

import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
		Thread serverThread = new Thread() {
			public void run() {
				try {
					ServerSocket server = new ServerSocket(3456);
					server.setSoTimeout(100);
					Socket socket = server.accept();
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String message = reader.readLine();
					String response = message.equals("PING") ? "OK" : "ERROR";
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
					writer.write(response);
					writer.write("\n");
					writer.flush();
					reader.close();
					writer.close();
					socket.close();
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		serverThread.start();
		Thread.sleep(10);
		GraftClient client = new GraftClient();
		client.connectTo(3456);
		String response = client.ping();
		assertEquals("OK", response);
	}

	@Test(expected=SocketTimeoutException.class)
	public void whatTheHellDoesSOTimeoutDo() throws IOException {
		ServerSocket s = new ServerSocket(12334);
		s.setSoTimeout(1);
		s.accept();
	}

}
