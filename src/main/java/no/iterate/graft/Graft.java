package no.iterate.graft;

import java.util.ArrayList;
import java.util.Collection;

public class Graft {

	private final Collection<Node> nodes = new ArrayList<Node>();

	public Node createNode() {
		Node node = new Node();
		nodes.add(node);
		return node;
	}

	public Node getNodeByProperty(String property, String value) {
		for (Node each : nodes) {
			if (value.equals(each.get(property))) {
				return each;
			}
		}
		return null;
	}

}
