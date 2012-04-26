package no.iterate.graft.client.remote;

import no.iterate.graft.Node;

import java.io.*;
import java.net.Socket;

public class GraftClient {

	private int port;

	public void connectTo(int port) throws IOException {
		this.port = port;
	}

	public Node createNode() {
		return new Node("1", null);
	}

	public void kill() {
	}

	public Node getNodeById(String id) {
		return new Node(id, null);
	}

	public void setReplica(int i) {
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
