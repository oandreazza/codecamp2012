package no.iterate.graft;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import no.iterate.geekolympics.Event;
import no.iterate.geekolympics.GeekOlympics;

import org.junit.Test;

public class AddCommentOnEventTest {

	@Test
	public void shouldAddACommentOnAnEvent() throws Exception {
		GeekOlympics server = connectToAServer();
		Event dashEvent = server.createEvent("100mDash");
		dashEvent.addComment("this is a cool dash!");

		Collection comments = server.getEventById("100mDash").getComments();

		assertEquals(1, comments.size());
		assertEquals("this is a cool dash!", comments.iterator().next());
	}

	private GeekOlympics connectToAServer() {
		return new GeekOlympics();
	}

}
