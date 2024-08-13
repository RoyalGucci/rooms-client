package net.rooms.client.connection.requests;

public record CreateRequest(
		String title,
		String description,
		boolean isPrivate,
		String password
) {
}