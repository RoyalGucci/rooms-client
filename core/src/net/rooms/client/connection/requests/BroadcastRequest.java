package net.rooms.client.connection.requests;

public record BroadcastRequest(
		long id, // Game ID
		String payload, // Json payload containing the game packet that needs transfer
		String jSessionID
) {
}
