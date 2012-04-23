package no.iterate.graft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import no.iterate.graft.Graft;
import no.iterate.graft.Node;

import org.junit.Test;

public class GraftTest {

	@Test
	public void shouldFindNodeByProperty() {
		Graft db = new Graft();
		Node node = db.createNode();
		node.put("id", "100mDash");
		node.put("comment", "this is a cool dash!");

		Node fetched = db.getNodeByProperty("id", "100mDash");

		assertNotNull(fetched);
		assertEquals("this is a cool dash!", fetched.get("comment"));
	}
}
