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
		System.out.println("[client] Connected to server " + host + ":" + portNumber);
		
		Socket socket = new Socket(host, portNumber);
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public Node createNode() throws IOException {
		try {
			writer.write("createNode");
			writer.newLine();
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String id = reader.readLine();
		System.out.println("[client] Created node id> " + id);
		return new Node(id, null);
	}

	public void kill() {
		try {
			writer.write("shutdown");
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Node getNodeById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
