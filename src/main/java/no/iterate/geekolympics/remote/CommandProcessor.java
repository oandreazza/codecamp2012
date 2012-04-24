package no.iterate.geekolympics.remote;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.iterate.geekolympics.GeekOlympics;

public class CommandProcessor {
	
	private final GeekOlympics geekOlympics = new GeekOlympics();
	private final static Pattern ADD_COMMENT = Pattern.compile("addComment (\\w*) ([\\w ]*)");
	
	public String process(String commandLine) {
		String[] parsed = commandLine.split(" ");
		String result = "[SYSTEM NOTICE: Your command was processed, lucky you!]";
	
		String command = parsed[0];
		if ("addEvent".equals(command)) {
			String eventID = parsed[1];
			addEvent(eventID);
		} else if ("addComment".equals(command)) {
			Matcher matcher = ADD_COMMENT.matcher(commandLine);
			
			matcher.find();
			String eventId = matcher.group(1);
			String comment = matcher.group(2);
	
			addComment(eventId, comment);
		} else if ("getComments".equals(command)) {
			String eventId = parsed[1];
			result = getComments(eventId);
		}
	
		return result;
	}

	private String getComments(String eventId) {
		StringBuilder result = new StringBuilder();
		Collection<String> comments = geekOlympics.getComments(eventId);

		for (String each : comments) {
			if (result.length() > 0) {
				result.append("\n");
			}
			result.append(each);
		}
		
		return result.toString();
	}

	private void addComment(String eventId, String comment) {
		geekOlympics.addComment(eventId, comment);
	}

	private void addEvent(String eventID) {
		geekOlympics.createEvent(eventID);
	}

}
