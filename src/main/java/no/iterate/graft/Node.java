package no.iterate.graft;

import java.util.HashMap;
import java.util.Map;

public class Node {

	private final Map<String, String> properties = new HashMap<String, String>();

	public void put(String property, String value) {
		properties.put(property, value);
	}

	public String get(String property) {
		return properties.get(property);
	}

}
