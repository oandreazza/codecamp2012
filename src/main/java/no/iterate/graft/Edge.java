package no.iterate.graft;

public class Edge extends PropertiesHolder {

	private final Node to;
	private final Node from;

	public Edge(String id, NodeListener graft, Node from, Node to) {
		super(id, graft);
		this.from = from;
		this.to = to;
	}

	public Node getTo() {
		return to;
	}

	public Node getFrom() {
		return from;
	}

}
