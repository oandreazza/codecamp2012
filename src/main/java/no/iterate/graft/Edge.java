package no.iterate.graft;

public class Edge extends PropertiesHolder {

	private final Node to;
	private final Node from;

	public Edge(String id, NodeListener graft, Node from, Node to) {
		super(id);
		this.from = from;
		this.to = to;
		addListener(graft);
	}

	public Node getTo() {
		return to;
	}

	public Node getFrom() {
		return from;
	}

}
