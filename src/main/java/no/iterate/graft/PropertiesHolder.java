package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class PropertiesHolder {

	protected final Map<String, String> properties = new HashMap<String, String>();
	protected final Collection<NodeListener> listeners = new ArrayList<NodeListener>();

	public void put(String property, String value) {
		properties.put(property, value);
		this.notifyListeners();
	}

	public String get(String property) {
		return properties.get(property);
	}

	protected void addListener(NodeListener myListener) {
		listeners.add(myListener);
	}

	protected Collection<NodeListener> getListeners() {
		return listeners;
	}

	private void notifyListeners() {
		for (NodeListener each : listeners) {
			each.update(this);
		}
	}

	public Map<String, String> getProperties() {
		return properties;
	}
}