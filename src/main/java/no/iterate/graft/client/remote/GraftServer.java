package no.iterate.graft.client.remote;

import no.iterate.graft.Edge;
import no.iterate.graft.Graft;
import no.iterate.graft.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class GraftServer {

	private final int port;
	private final Graft db;
	private ServerSocket server;

	public static GraftServer start(int port) {
		return start(port, new Graft());
	}

	public static GraftServer start(int port, Graft db) {
		GraftServer graftServer = new GraftServer(port, db);
		graftServer.start();
		return graftServer;
	}

	public GraftServer(int port, Graft db) {
		this.port = port;
		this.db = db;
	}

	public void start() {

		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("SERVER failed to start " + e);
			return;
		}

		new Thread() {
			public void run() {
				try {
					while (!server.isClosed()) {
						step(server);
					}
				} catch (SocketTimeoutException e) {
					// Hey, no problem mon
				} catch (SocketException e) {
					if (server.isClosed()) {
						System.err.println("SERVER Closed already");
					} else {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void step(ServerSocket server) throws IOException {
		Socket client = server.accept();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			String message = reader.readLine();
			String response = processMessage(message);
			PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
			writer.println(response);
		} finally {
			client.close();
		}
	}

	private String processMessage(String message) {
		if (message.equals("createNode")) {
			Node node = db.createNode();
			return node.getId();
		} else if (message.startsWith("getNodeById")) {
			String[] parsedMessage = message.split(" ");
			String id = parsedMessage[1];
			Node node = db.getNodeByProperty("id", id);
			return node.getId();
		} else if (message.startsWith("propagateNode ")) {
			String[] parsedMessage = message.split(" ");
			String id = parsedMessage[1];
			db.applyPropagatedNode(new Node(id, null));
			return "OK";
		} else if (message.startsWith("propagateEdge")) {
			String[] parsed = message.split(" ");
			String edgeId = parsed[1];
			String fromNodeId = parsed[2];
			String toNodeId = parsed[3];

			Node from = db.getNodeByProperty("id", fromNodeId);
			Node to = db.getNodeByProperty("id", toNodeId);
			Edge edge = new Edge(edgeId, db, from, to);
			db.applyPropagatedEdge(edge);

			return "OK";
		} else if (message.startsWith("propagateProperties")) {
			String[] parsed = message.split(" ");
			Map<String, String> properties = new HashMap<String, String>();
			
			for (int i = 1; i < parsed.length; i++) {
				String[] string = parsed[i].split(":");
				properties.put(string[0], string[1]);
			}
			db.applyPropagatedProperties(properties);
			
			return "OK";
		} else if (message.startsWith("setReplica ")) {
			String[] parsed = message.split(" ");
			String hostname = parsed[1];
			int port = Integer.parseInt(parsed[2]);
			db.setReplica(hostname, port);
			
			return "OK";
		} else if (message.equals("PING")) {
			return "OK";
		} else if (message.equals("kill")) {
			db.kill();
			return "OK";
		} else {
			System.out.println("SERVER Unknown command " + message);
			return "ERROR";
		}
	}

	public void die() {
		try {
			server.close();
		} catch (IOException e) {
			System.err.println("SERVER rejected to die " + e);
		}
	}
}
