package no.iterate.graft.client.remote;

import no.iterate.graft.Node;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class GraftClient {

	private int port;

	public void connectTo(int port) throws IOException {
		this.port = port;
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
		Socket clientSocket = null;
		try {
			clientSocket = new Socket("localhost", port);
		} catch (IOException e) {
			return "ERROR";
		}
		
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
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
