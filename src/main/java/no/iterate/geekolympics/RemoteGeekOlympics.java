package no.iterate.geekolympics;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RemoteGeekOlympics {

	static final int CONSOLE_PORT = 1234;

	public RemoteGeekOlympics() {
		// start the Groovy Console server ....
	}

	public String send(String command) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("localhost", CONSOLE_PORT));
			socket.getOutputStream().write(command.getBytes());
			// TBD: socket.getInputStream().read() ...
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {}
		}
		return "whatever I really expect... (TBD)";
	}

}
