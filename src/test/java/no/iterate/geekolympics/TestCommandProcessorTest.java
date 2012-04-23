package no.iterate.geekolympics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestCommandProcessorTest {
	private CommandProcessor subject;

	@Before
	public void before() {
		this.subject = new CommandProcessor();
	}

	@Test
	public void shouldAddComment() throws Exception {
		subject.process("addEvent eventId");
		subject.process("addComment eventId comment");
		String result = subject.process("getComments eventId");
		assertEquals(result, "comment");
	}
}