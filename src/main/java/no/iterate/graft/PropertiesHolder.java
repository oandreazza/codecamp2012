package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class PropertiesHolder {

	private final Map<String, String> properties = new HashMap<String, String>();
	protected final Collection<NodeListener> listeners = new ArrayList<NodeListener>();

	public PropertiesHolder(String id, NodeListener graft) {
		put("id", id);
		addListener(graft);
	}

	public void put(String property, String value) {
		properties.put(property, value);
		this.notifyListeners();
	}

	public String get(String property) {
		return properties.get(property);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	protected void addListener(NodeListener myListener) {
		listeners.add(myListener);
	}

	protected Collection<NodeListener> getListeners() {
		return listeners;
	}

	protected void setProperties(Map<String, String> properties) {
		this.properties.clear();
		this.properties.putAll(properties);
	}

	void notifyListeners() {
		for (NodeListener each : listeners) {
			each.update(this);
		}
	}

	public String getId() {
		return get("id");
	}
}