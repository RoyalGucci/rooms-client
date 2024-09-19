package net.rooms.client.connection.requests;

public record JoinRequest(
		long roomID,
		String password
) {
}
