package no.iterate.graft.client.remote;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import jline.internal.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GraftServerProtocolIT {
	
	private static final int port = 1234;
	private GraftServer server;
	
	@Before
	public void setUp() {
		server = GraftServer.start(port);
	}
	
	@After
	public void shutDown() {
		server.stop();
	}
	
	@Test(timeout=1000)
	public void serverCreateNode() throws Exception {
		Socket client = new Socket("localhost", port);
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		out.println("createNode");
		
		assertEquals("0", in.readLine());
	}
	
	@Test(timeout=1000)
	public void serverReturnNodeById() throws Exception {
		Socket client = new Socket("localhost", port);
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		out.println("createNode");
		in.readLine();
		
		out.println("getNodeById 0");
		
		assertEquals("id=0", in.readLine());
	}
	
	@Test(timeout=1000)
	public void serverReturnNodeByIdForTwoNodes() throws Exception {
		Socket client = new Socket("localhost", port);
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		out.println("createNode");
		in.readLine();
		out.println("createNode");
		in.readLine();
		
		out.println("getNodeById 1");
		
		assertEquals("id=1", in.readLine());
	}
	
	@Test(timeout=1000)
	public void serverReturnErrorWhenNoSuchNode() throws Exception {
		Socket client = new Socket("localhost", port);
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		out.println("getNodeById 0");
		
		assertEquals("ERROR", in.readLine());
	}
	
}
