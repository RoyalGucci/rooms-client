package net.rooms.client.connection.objects;

public record BroadcastNotification(
		String payload // Json payload containing the game packet that needs transfer
) {
}
