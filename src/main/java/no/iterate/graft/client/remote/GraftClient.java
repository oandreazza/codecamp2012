package no.iterate.graft.client.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import jline.internal.InputStreamReader;

import no.iterate.graft.Node;

public class GraftClient {

	private BufferedWriter writer;
	private BufferedReader reader;

	public void connectTo(GraftServer server) throws UnknownHostException,
			IOException {
		String host = "localhost";
		int portNumber = server.getPortNumber();
		System.out.println("[client] Connected to server " + host + ":"
				+ portNumber);

		Socket socket = new Socket(host, portNumber);
		writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
	}

	public Node createNode() throws IOException {
		sendToServer(GraftServer.CREATE_NODE_COMMAND);

		String id = reader.readLine();
		return new Node(id, null);
	}

	public void kill() {
		sendToServer(GraftServer.SHUTDOWN_COMMAND);
	}

	public Node getNodeById(String id) throws IOException {
		sendToServer(GraftServer.GET_NODE_BY_ID_COMMAND + " " + id);

		String nodeString = reader.readLine();
		System.out.println("[client] getNodeById> " + nodeString);
		Node recievedNode = createFromString(nodeString);

		return recievedNode;
	}

	private void sendToServer(String command) {
		try {
			writer.write(command);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[client] SendToServer> " + command);
	}

	private Node createFromString(String id) {
		Node receivedNode = new Node(id, null);
		return receivedNode;
	}

	public void setReplica(int port) throws IOException {
		sendToServer("setReplica " + port);
		
		String result = reader.readLine();
		System.out.println("[client] addReplicaResponse> " + result);
	}

}
