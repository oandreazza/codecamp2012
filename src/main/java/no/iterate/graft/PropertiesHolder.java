package no.iterate.graft;

import java.util.HashMap;
import java.util.Map;

public abstract class PropertiesHolder {

	private final Map<String, String> properties = new HashMap<String, String>();

	public PropertiesHolder() {
		super();
	}

	public void put(String property, String value) {
		properties.put(property, value);
	}

	public String get(String property) {
		return properties.get(property);
	}

}