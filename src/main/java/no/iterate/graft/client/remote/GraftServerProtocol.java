package no.iterate.graft.client.remote;

import java.util.Map.Entry;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;

public class GraftServerProtocol {
	
	private final Graft db = new Graft();
	
	@Deprecated
	public Graft getDb() {
		return db;
	}

	private String createNode() {
		return db.createNode().getId();
	}

	private String getNodeById(String nodeId) {
		Node node = db.getNodeByProperty("id", nodeId);
		return serialize(node);
	}

	private String serialize(Node node) {
		StringBuilder serialized = new StringBuilder();
		
		for (Entry<String, String> each : node.getProperties().entrySet()) {
			serialized.append(each.getKey())
				.append("=")
				.append(each.getValue());
		}
		return serialized.toString();
	}

	public String process(String commandLine) {
		String[] parsed = commandLine.split(" ");
		String command = parsed[0];
		
		try {
			return tryProcess(command, parsed);
		} catch (Exception e) {
			return "ERROR";
		}
		
	}

	private String tryProcess(String command, String[] arguments) {
		if ("createNode".equals(command)) {
			return createNode();
		} else if ("getNodeById".equals(command)) {
			return getNodeById(arguments[1]);
		}
		
		return command; // TODO throw an exception
	}

}
