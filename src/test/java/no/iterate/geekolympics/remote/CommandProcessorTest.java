package no.iterate.geekolympics.remote;

import static org.junit.Assert.*;

import no.iterate.geekolympics.remote.CommandProcessor;
import no.iterate.graft.Graft;

import org.junit.Before;
import org.junit.Test;

public class CommandProcessorTest {

	private CommandProcessor subject;

	@Before
	public void before() {
		Graft db = new Graft();
		this.subject = new CommandProcessor(db);
	}

	@Test
	public void addComment() throws Exception {
		subject.process("addEvent eventId");
		subject.process("addComment eventId comment");
		String result = subject.process("getComments eventId");
		assertEquals("comment", result);
	}

	@Test
	public void multipleCommentsAreSeparatedByNewlinesFor2Comments()
			throws Exception {
		subject.process("addEvent eventId");
		subject.process("addComment eventId comment1");
		subject.process("addComment eventId comment2");
		String result = subject.process("getComments eventId");
		assertEquals("comment1\ncomment2", result);
	}

	@Test
	public void multipleCommentsAreSeparatedByNewlinesFor3Comments()
			throws Exception {
		subject.process("addEvent eventId");
		subject.process("addComment eventId comment1");
		subject.process("addComment eventId comment2");
		subject.process("addComment eventId comment3");
		String result = subject.process("getComments eventId");
		assertEquals("comment1\ncomment2\ncomment3", result);
	}

	@Test
	public void addMultiWordComment() throws Exception {
		subject.process("addEvent eventId");
		subject.process("addComment eventId multiword comment");
		String result = subject.process("getComments eventId");
		assertEquals("multiword comment", result);
	}
}
