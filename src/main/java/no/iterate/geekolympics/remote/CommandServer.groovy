package no.iterate.geekolympics

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.codehaus.groovy.tools.shell.Groovysh
import no.iterate.geekolympics.remote.CommandProcessor;

class CommandServer {

	public static void main(String[] args) {
		startServer()
	}

	public static void startServer() {
		final ServerSocket server = new ServerSocket(33333);
		final CommandProcessor commander = new CommandProcessor()

		def serverThread = new Thread() {

			public void run() {
				println "Behold, the GroovyShell Server is alive!!!"
				while(true) {
					server.accept {
						socket ->
						println "Socket connection established"
						socket.withStreams {
							input, output ->
								println "Instantiating shell"

								BufferedReader inLines = new BufferedReader(new InputStreamReader(input))
								def commandLine
								while ((commandLine = inLines.readLine()) != null) {
									System.out.write(commandLine.getBytes())
									output.write((commander.process(commandLine) + '\n').getBytes())
								}

								println "Shell exit"
						}
					}
				}
			}
		};

		serverThread.start();
	}

}
