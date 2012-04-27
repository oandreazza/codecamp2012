package no.iterate.graft.client.remote;

import no.iterate.graft.Edge;
import no.iterate.graft.Node;
import no.iterate.graft.PropertiesHolder;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;

public class GraftClient {

	private final int port;
	private final String hostname;
	private String user;

	public GraftClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public Node createNode() {
		String message = sendMessage("createNode");
		return new Node(message, null);
	}

	public Node getNodeById(String id) {
		String message = sendMessage("getNodeById " + id);
		return new Node(message, null);
	}

	public void propagateNode(Node node) {
		sendMessage("propagateNode " + node.getId());
	}

	public void propagateEdge(Edge edge) {
		sendMessage("propagateEdge " + edge.getId() + " "
				+ edge.getFrom().getId() + " " + edge.getTo().getId());
	}

	public void propagateProperties(PropertiesHolder target) {
		String message = "propagateProperties ";
		for (Entry<String, String> each : target.getProperties().entrySet()) {
			message += each.getKey() + ":" + each.getValue() + " ";
		}
		sendMessage(message);
	}

	public String ping() {
		return sendMessage("PING");
	}

	private String sendMessage(String message) {

		Socket clientSocket;
		try {
			clientSocket = new Socket(hostname, port);
		} catch (IOException e) {
			return "ERROR";
		}

		try {
			PrintWriter writer = new PrintWriter(
					clientSocket.getOutputStream(), true);
			writer.println(message);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setReplica(String hostname, int port) {
		sendMessage("setReplica "+ hostname + " " + port);
	}

	public void logIn(String userName) {
		user = userName;
	}

	public void postComment(String string) {
		return;
	}

	public Collection<String> getNotifications() {
		// TODO Auto-generated method stub
		return Arrays.asList("comment");
	}
}
