package no.iterate.graft.client.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

class GraftServer {
	private Thread serverThread;
	private boolean isRunning;
	private int port;

	GraftServer(int port) {
		this.port = port;
	}

	public void invoke() {
		 serverThread = new Thread() {
			public void run() {
				try {
					ServerSocket server = new ServerSocket(port);
					server.setSoTimeout(1000);
					try {
						while (isRunning) {
							once(server);
						}
					} catch (SocketTimeoutException e) {
						e.printStackTrace();
					} finally {
						server.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		isRunning = true;
		serverThread.start();
	}

	private void once(ServerSocket server) throws IOException {
		Socket socket = server.accept();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = reader.readLine();
			String response = message.equals("PING") ? "OK" : "ERROR";
			OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
			writer.write(response);
			writer.write("\n");
			writer.flush();
		} finally {
			socket.close();
		}
	}

	public static GraftServer start(int port) {
		GraftServer graftServer = new GraftServer(port);
		graftServer.invoke();
		return graftServer;
	}

	public void addReplica(GraftServer second) {
	}

	public void die() {
		isRunning = false;
	}
}
