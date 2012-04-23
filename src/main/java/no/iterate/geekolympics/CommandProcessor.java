package no.iterate.geekolympics;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {
	private static final String REGEX_PATTERN = "addComment (\\w*) ([\\w ]*)";
	private GeekOlympics geekOlympics = new GeekOlympics();

	public String process(String commandLine) {
		String[] parsed = commandLine.split(" ");
		String result = "[SYSTEM NOTICE: Your command was processed, lucky you!]";

		String command = parsed[0];
		if ("addEvent".equals(command)) {
			String eventID = parsed[1];
			addEvent(eventID);
		} else if ("addComment".equals(command)) {
			Pattern pattern = Pattern.compile(REGEX_PATTERN);
			Matcher matcher = pattern.matcher(commandLine);
			
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

		boolean isFirst = false;
		int size = comments.size();
		int counter = 1;
		for (String each : comments) {
			result.append(each);
			if (!isFirst && size != 1) {
				isFirst = true;

				if (counter < size) {
					result.append("\n");
				}
			}

			counter++;
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
