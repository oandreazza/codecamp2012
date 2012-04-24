package no.iterate.graft;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import no.iterate.geekolympics.GeekOlympics;

import org.junit.Test;

public class AddCommentOnEventTest {

	@Test
	public void addACommentOnAnEvent() throws Exception {
		GeekOlympics server = connectToAServer();
		server.createEvent("100mDash");
		server.addComment("100mDash", "this is a cool dash!");

		Collection<String> comments = server.getComments("100mDash");

		assertEquals(1, comments.size());
		assertEquals("this is a cool dash!", comments.iterator().next());
	}

	@Test
	public void addCommentsOnAnEvent() throws Exception {
		GeekOlympics server = connectToAServer();
		server.createEvent("100mDash");
		server.addComment("100mDash", "this is a cool dash!");
		server.addComment("100mDash", "next comment");

		Collection<String> comments = server.getComments("100mDash");

		assertEquals(2, comments.size());
	}

	private GeekOlympics connectToAServer() {
		return new GeekOlympics();
	}

}
