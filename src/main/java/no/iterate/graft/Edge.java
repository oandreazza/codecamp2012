package no.iterate.graft;

public class Edge extends PropertiesHolder {

	public Edge(String id, NodeListener graft) {
		super(id);
		addListener(graft);
	}

}
