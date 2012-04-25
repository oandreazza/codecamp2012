package no.iterate.graft;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GraftSubscriptions {
	Map<String, Collection<IGraftSubscriber>> subscriptions = new HashMap<String, Collection<IGraftSubscriber>>();

	public void subscribe(Node node, IGraftSubscriber subscriber) {
		Collection<IGraftSubscriber> subscribers = subscriptions.get(node.getId());
		if (subscribers == null) {
			subscribers = new HashSet<IGraftSubscriber>();
			subscriptions.put(node.getId(), subscribers);
		}
		subscribers.add(subscriber);
	}

	public void notifySubscribers(Edge target) {
		String eventId = target.getFrom().getId();
		Collection<IGraftSubscriber> collection = subscriptions.get(eventId);
		if (collection == null) {
			return; // Never mind...
		}

		for (IGraftSubscriber each : collection) {
			each.notify(target);
		}
	}
}
