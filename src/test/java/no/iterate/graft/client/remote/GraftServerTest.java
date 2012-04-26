package no.iterate.graft.client.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import jline.internal.InputStreamReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class GraftServerTest {
	
	private static final int port = 1234;
	private static GraftServer server;
	
	@BeforeClass
	public static void setUp() {
		server = GraftServer.start(port);
	}
	
	@AfterClass
	public static void shutDown() {
		server.stop();
	}
	
	@Test
	public void serverListensForConnections() throws Exception {
		Socket client = new Socket("localhost", port);
		
		// We expect NO exception
		assertNotNull(client);
	}
	
	@Test(timeout=1000)
	public void serverSendsResponseForCreateNode() throws Exception {
		
		Socket client = new Socket("localhost", port);
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		//out.println("hello");
		String response = in.readLine();
		
		// We expect NO exception
		assertEquals("ok", response);
	}

}