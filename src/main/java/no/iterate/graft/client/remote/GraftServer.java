package no.iterate.graft.client.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;

public class GraftServer implements Runnable {

	public static final String GET_NODE_BY_ID_COMMAND = "getNodeById";
	public static final String CREATE_NODE_COMMAND = "createNode";
	public static final String SHUTDOWN_COMMAND = "shutdown";
	private final int port;
	private final List<GraftServer> replicas = new LinkedList<GraftServer>();
	private final Graft db = new Graft();

	private GraftServer(int port) {
		this.port = port;
	}

	public static GraftServer start(int port) throws IOException,
			ClassNotFoundException, InterruptedException {
		GraftServer server = new GraftServer(port);
		new Thread(server).start();

		return server;
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		GraftServer.start(9999);
	}

	@Override
	public void run() {
		ServerSocket socket = null;
		Socket connection = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;

		try {
			socket = new ServerSocket(port);
			connection = socket.accept();

			String message = "";
			do {
				if (connection.isClosed()) {
					connection = socket.accept();
				}

				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(
						connection.getOutputStream()));

				message = (String) reader.readLine();
				if (message == null) {
					connection.close();
				} else {
					System.out.println("[server" + port + "] Received> "
							+ message);

					if (CREATE_NODE_COMMAND.equals(message)) {
						Node node = db.createNode();
						String nodeId = node.getId();
						sendResponse(writer, nodeId);

						addNodeToReplicas();
					} else if (message.startsWith(GET_NODE_BY_ID_COMMAND)) {
						String[] parsedMessage = message.split(" ");
						Node node = db
								.getNodeByProperty("id", parsedMessage[1]);
						sendResponse(writer, node.getId());
					}
				}
			} while (!SHUTDOWN_COMMAND.equals(message));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
				connection.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getPortNumber() {
		return port;
	}

	public void addReplica(GraftServer server) {
		replicas.add(server);
	}

	private void sendResponse(BufferedWriter writer, String message)
			throws IOException {
		writer.write(message);
		writer.newLine();
		writer.flush();
	}

	private void addNodeToReplicas() {
		for (GraftServer each : replicas) {
			Socket socket = null;
			try {
				socket = new Socket("localhost", each.getPortNumber());
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream()));
				sendResponse(writer, CREATE_NODE_COMMAND);
				socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
