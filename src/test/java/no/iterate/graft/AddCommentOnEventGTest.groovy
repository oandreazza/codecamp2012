package no.iterate.graft;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import no.iterate.geekolympics.Event;
import no.iterate.geekolympics.GeekOlympics;
import no.iterate.geekolympics.RemoteGeekOlympics;

import org.junit.Ignore;
import org.junit.Test;

public class AddCommentOnEventGTest {

	@Test
	public void shouldAddACommentOnAnEvent() throws Exception {
		GeekOlympics server = connectToAServer();
		Event dashEvent = server.createEvent("100mDash");
		dashEvent.addComment("this is a cool dash!");

		Collection comments = server.getEventById("100mDash").getComments();

		assert comments == ["this is a cool dash!"]
	}

	@Ignore("Implementation postponed after UI done")
	@Test
	public void shouldAddACommentsOnAnEvent() throws Exception {
		GeekOlympics server = connectToAServer();
		Event dashEvent = server.createEvent("100mDash");
		dashEvent.addComment("this is a cool dash!");
		dashEvent.addComment("next comment");

		Collection comments = server.getEventById("100mDash").getComments();

		assert comments.size() == 2
	}

	@Test
	public void shouldSendCommandToServer() throws Exception {
		RemoteGeekOlympics server = new RemoteGeekOlympics();
		def addCommentResponse = server.send("getInstance().createEvent('100mDash').addComment('this is a cool dash!')");
		assert addCommentResponse == "whatever I really expect... (TBD)"
	}

	private GeekOlympics connectToAServer() {
		return new GeekOlympics();
	}



}
