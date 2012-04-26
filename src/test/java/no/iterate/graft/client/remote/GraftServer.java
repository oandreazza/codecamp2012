package no.iterate.graft.client.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.management.RuntimeErrorException;

import jline.internal.InputStreamReader;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;

public class GraftServer implements Runnable {

	private Graft graft = new Graft();
	private final int port;
	private ServerSocket serverSocket;

	public GraftServer() {
		this(4321);
	}

	public static void main(String[] args) {
		GraftServer.start(9999);
	}

	private GraftServer(int port) {
		this(port, false);
	}

	private GraftServer(int port, boolean start) {
		this.port = port;
		if (start) {
			serverSocket = initServerSocket(port);
		}
	}

	private static ServerSocket initServerSocket(int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);
			System.err.println("SERVER started on " + port);
			return serverSocket;
		} catch (IOException e) {
			throw new RuntimeException(
					"Server start failed> " + e.getMessage(), e);
		}
	}

	public static GraftServer start(int port) {
		GraftServer server = new GraftServer(port, true);
		new Thread(server).start();

		return server;
	}

	public void addReplica(GraftServer replica) {
		graft.addReplica(replica.graft);
	}

	public Node createNode() {
		return graft.createNode();
	}

	public Node getNodeById(String id) {
		return graft.getNodeByProperty("id", id);
	}

	@Override
	public void run() {
		try {
			Socket client;
			while ((client = serverSocket.accept()) != null) {

				PrintWriter out = new PrintWriter(client.getOutputStream(),
						true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						client.getInputStream()));

				String input;
				while ((input = in.readLine()) != null) {

					System.err.println("SERVER GOT " + input);
					
					if ("shutdown".equals(input)) {
						client.close();
					} else {
						out.println(input + " to you too");
					}
				}
			}

		} catch (SocketException e) {
			if (serverSocket.isClosed()) {
				System.err
						.println("SERVER Socket has been closed, stopping the thread");
			} else {
				System.err.println("SERVER socket exception " + e);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVER Unexpected exception " + e);
		}
	}

	public void stop() {
		try {
			System.out.println("SERVER closing at " + port);
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("SERVER close failed " + e);
		}
	}

}
