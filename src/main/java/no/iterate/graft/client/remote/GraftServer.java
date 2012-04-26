package no.iterate.graft.client.remote;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;

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
	private Graft db = new Graft();
	private boolean isClosed = false;

	GraftServer(int port) {
		this.port = port;
	}

	public void start() {
		 serverThread = new Thread() {
			public void run() {
				try {
					ServerSocket server = new ServerSocket(port);
					server.setSoTimeout(100);
					try {
						while (isRunning) {
							once(server);
						}
					} catch (SocketTimeoutException e) {
						// Hey, no problem mon
					} finally {
						server.close();
						isClosed = true;
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
			String response = processMessage(message);
			OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
			writer.write(response);
			writer.write("\n");
			writer.flush();
		} finally {
			socket.close();
		}
	}

	private String processMessage(String message) {
		if(message.equals("createNode")) {
			Node node = db.createNode();
			return node.getId();
		} else if(message.startsWith("getNodeById")) {
			String[] parsedMessage = message.split(" ");
			String id = parsedMessage[1];
			Node node = db.getNodeByProperty("id", id);
			return node.getId();
		} else if (message.equals("kill")) {
			db.kill();
			return "OK";
		} else {
			return "OK";
		}
	}

	public static GraftServer start(int port) {
		GraftServer graftServer = new GraftServer(port);
		graftServer.start();
		return graftServer;
	}

	public void addReplica(GraftServer second) {
		db.addReplica(second.db);
	}

	public void die() {
		isRunning = false;
		while (! isClosed) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
	}
}
