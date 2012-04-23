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
		StringBuilder result = new StringBuilder();
		Event event = geekOlympics.getEventById(eventId);
		Collection<String> comments = (Collection<String>) event.getComments();

		boolean isFirst = false;
		int size = comments.size();
		int counter = 0;
		for (String each : comments) {
			result.append(each);
			if (!isFirst && size != 1) {
				isFirst = true;
				
				if(counter < size) {
					result.append("\n");
				}
			}
			
			counter++;
		}

		return result.toString();
	}

	private void addComment(String eventId, String comment) {
		Event event = geekOlympics.getEventById(eventId);
		event.addComment(comment);
	}

	private void addEvent(String eventID) {
		geekOlympics.createEvent(eventID);
	}

}
