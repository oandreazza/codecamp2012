package no.iterate.graft.client.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GraftServer implements Runnable {

	private static final String SHUTDOWN_COMMAND = "shutdown";
	private final int port;

	public GraftServer(int port) {
		this.port = port;
	}

	public static GraftServer start(int port) throws IOException,
			ClassNotFoundException, InterruptedException {
		GraftServer server = new GraftServer(port);
		new Thread(server).start();

		Thread.sleep(2000);

		return server;
	}

	@Override
	public void run() {
		String message = "";
		BufferedReader reader = null;
		BufferedWriter writer;
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
				reader = new BufferedReader(new InputStreamReader(
						serverSocket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(
						serverSocket.getOutputStream()));

				message = (String) reader.readLine();
				if ("createNode".equals(message)) {
					String nodeId = "10";
					writer.write(nodeId);
					writer.newLine();
					writer.flush();
				} else if (message.startsWith("getNodeById")) {
					writer.write("10");
					writer.newLine();
					writer.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("[server] Received> " + message);
		} while (!SHUTDOWN_COMMAND.equals(message));

		try {
			reader.close();
			serverSocket.close();
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

}
