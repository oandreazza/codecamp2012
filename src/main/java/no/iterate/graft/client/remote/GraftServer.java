package no.iterate.graft.client.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;

public class GraftServer implements Runnable {

	private static final String SHUTDOWN_COMMAND = "shutdown";
	private final int port;
	private List<GraftServer> replicas = new LinkedList<GraftServer>();
	private final Graft db;

	public GraftServer(int port, Graft db) {
		this.port = port;
		this.db = db;
	}

	public static GraftServer start(int port) throws IOException,
			ClassNotFoundException, InterruptedException {
		Graft db = new Graft();

		GraftServer server = new GraftServer(port, db);
		new Thread(server).start();

		return server;
	}

	@Override
	public void run() {
		String message = "";
		BufferedReader reader = null;
		BufferedWriter writer = null;

		Socket serverSocket = null;
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
			serverSocket = socket.accept();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		do {
			try {
				if (serverSocket.isClosed()) {
					serverSocket = socket.accept();
				}
				
				reader = new BufferedReader(new InputStreamReader(
						serverSocket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(
						serverSocket.getOutputStream()));

				message = (String) reader.readLine();

				if (message == null) {
					serverSocket.close();
				} else {

					System.out.println("[server" + port + "] Received> "
							+ message);

					if ("createNode".equals(message)) {
						Node node = db.createNode();
						String nodeId = node.getId();

						writer.write(nodeId);
						writer.newLine();
						writer.flush();

						addNodeToReplicas();
					} else if (message.startsWith("getNodeById")) {
						String[] parsedMessage = message.split(" ");
						Node node = db
								.getNodeByProperty("id", parsedMessage[1]);
						writer.write(node.getId());
						writer.newLine();
						writer.flush();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (!SHUTDOWN_COMMAND.equals(message));

		try {
			reader.close();
			writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		GraftServer.start(9999);
	}

	public int getPortNumber() {
		return port;
	}

	private void addNodeToReplicas() throws IOException {
		for (GraftServer each : replicas) {
			Socket socket = null;
			socket = new Socket("localhost", each.getPortNumber());

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			writer.write("createNode");
			writer.newLine();
			writer.flush();
			socket.close();
		}
	}

	public void addReplica(GraftServer server) {
		replicas.add(server);
	}

}
