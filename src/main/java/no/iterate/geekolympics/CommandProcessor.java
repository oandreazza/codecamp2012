package no.iterate.geekolympics;

import java.util.Collection;

public class CommandProcessor {
	private GeekOlympics geekOlympics = new GeekOlympics();

	public String process(String commandLine) {
		String[] parsed = commandLine.split(" ");
		String result = "[SYSTEM NOTICE: Your command was processed, lucky you!]";

		String command = parsed[0];
		if ("addEvent".equals(command)) {
			String eventID = parsed[1];
			addEvent(eventID);
		} else if ("addComment".equals(command)) {
			String eventId = parsed[1];
			String comment = parsed[2];
			addComment(eventId, comment);
		} else if ("getComments".equals(command)) {
			String eventId = parsed[1];
			result = getComments(eventId);
		}

		return result;
	}

	private String getComments(String eventId) {
		Event event = geekOlympics.getEventById(eventId);
		Collection comments = event.getComments();

		return (String) comments.iterator().next();
	}

	private void addComment(String eventId, String comment) {
		Event event = geekOlympics.getEventById(eventId);
		event.addComment(comment);
	}

	private void addEvent(String eventID) {
		geekOlympics.createEvent(eventID);
	}

}
