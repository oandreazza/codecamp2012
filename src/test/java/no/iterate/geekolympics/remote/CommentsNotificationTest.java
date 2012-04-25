package no.iterate.geekolympics.remote;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import no.iterate.graft.Graft;

import org.junit.Test;

public class CommentsNotificationTest {

	@Test
	public void receiveCommentNotificationsFor2Users() throws Exception {
		Graft db = new Graft();
		CommandProcessor user1 = new CommandProcessor(db);
		user1.process("addEvent 100mDash");
		user1.process("login user1");
		user1.process("addComment 100mDash whatever");
		CommandProcessor user2 = new CommandProcessor(db);
		user2.process("login user2");
		user2.process("addComment 100mDash dude");

		Collection<String> notifications = user1.getNotifications();
		String notification = notifications.iterator().next();

		assertEquals("user2: commented on 100mDash dude", notification);
	}
	
	@Test
	public void receiveCommentNotificationsFor3Users() throws Exception {
		Graft db = new Graft();
		CommandProcessor user1 = new CommandProcessor(db);
		user1.process("addEvent 100mDash");
		user1.process("login user1");
		user1.process("addComment 100mDash whatever");
		CommandProcessor user2 = new CommandProcessor(db);
		user2.process("login user2");
		user2.process("addComment 100mDash dude");
		CommandProcessor user3 = new CommandProcessor(db);
		user3.process("login user3");
		user3.process("addComment 100mDash new");

		Collection<String> notifications = user1.getNotifications();
		String notification = notifications.iterator().next();

		assertEquals("user2: commented on 100mDash dude", notification);
		assertEquals(2, user1.getNotifications().size());
		assertEquals(1, user2.getNotifications().size());
	}

	@Test
	public void subscribersAreOnlyAddedOnce() throws Exception {
		Graft db = new Graft();
		CommandProcessor user1 = new CommandProcessor(db);
		user1.process("addEvent 100mDash");
		user1.process("login user1");
		user1.process("addComment 100mDash whatever");
		user1.process("addComment 100mDash whatever2");
		CommandProcessor user2 = new CommandProcessor(db);
		user2.process("login user2");
		user2.process("addComment 100mDash dude");

		Collection<String> notifications = user1.getNotifications();
		
		assertEquals(2, notifications.size());
	}

}
