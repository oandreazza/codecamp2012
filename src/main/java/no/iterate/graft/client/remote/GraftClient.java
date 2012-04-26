package no.iterate.graft.client.remote;

import no.iterate.graft.Node;

import java.io.*;
import java.net.Socket;

public class GraftClient {

	private int port;

	public GraftClient(int port) {
		this.port = port;
	}

	public void connectTo(int port) throws IOException {
		this.port = port;
	}

	public Node createNode() {
		String message = sendMessage("createNode");
		return new Node(message, null);
	}

	public void kill() {
		sendMessage("kill");
	}

	public Node getNodeById(String id) {
		String message = sendMessage("getNodeById " + id);
		return new Node(message, null);
	}

	public String ping() {
		return sendMessage("PING");
	}

	private String sendMessage(String message) {
		Socket clientSocket = null;
		try {
			clientSocket = new Socket("localhost", port);
		} catch (IOException e) {
			return "ERROR";
		}

		try {
			OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
			writer.write(message);
			writer.write("\n");
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
}
