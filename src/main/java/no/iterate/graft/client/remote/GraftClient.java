package no.iterate.graft.client.remote;

import no.iterate.graft.Node;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class GraftClient {

	private Socket clientSocket;

	public void connectTo(int port) throws IOException {
		clientSocket = new Socket("localhost", port);
	}

	public Node createNode() {
		return null;
	}

	public void kill() {
	}

	public Node getNodeById(String id) {
		return null;
	}

	public void setReplica(int i) {
	}

	public String ping() {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
			writer.write("PING");
			writer.write("\n");
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
}
