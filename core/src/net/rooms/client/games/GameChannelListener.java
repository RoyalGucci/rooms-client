package net.rooms.client.games;

import net.rooms.client.Client;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.BroadcastNotification;

import java.util.function.Consumer;

/**
 * Provides a simple interface for setting and removing websocket listeners for the game host and
 * guest channels.
 */
public class GameChannelListener {

	private final Client client;

	public GameChannelListener(Client client) {
		this.client = client;
	}

	/**
	 * Used by the host to listen to packets arriving from guests (other players).
	 *
	 * @param consumer Operation to invoke upon receiving a packet.
	 * @param type     The expected type of the packet (depends on the hosted game).
	 */
	public <T> void setHostChannelListener(Consumer<T> consumer, Class<T> type) {
		client.getApiRequests().setWSListener("game/host-channel", notification -> consumer.accept(JSON.fromJson(notification.payload(), type)), BroadcastNotification.class);
	}

	/**
	 * Used by a guest to listen to packets arriving from the host.
	 *
	 * @param consumer Operation to invoke upon receiving a packet from the host.
	 * @param type     The expected type of the packet (depends on the game).
	 */
	public <T> void setGuestChannelListener(Consumer<T> consumer, Class<T> type) {
		client.getApiRequests().setWSListener("game/guest-channel", notification -> consumer.accept(JSON.fromJson(notification.payload(), type)), BroadcastNotification.class);
	}

	/**
	 * Used by the host when the game is finished.
	 */
	public <T> void removeHostChannelListener() {
		client.getApiRequests().removeWSListener("game/host-channel");
	}

	/**
	 * Used by a guest when the game is finished.
	 */
	public <T> void removeGuestChannelListener() {
		client.getApiRequests().removeWSListener("game/guest-channel");
	}
}
