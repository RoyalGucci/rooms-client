package net.rooms.client.connection.requests;

public record InviteRequest(
		long roomID,
		String username
) {
}
