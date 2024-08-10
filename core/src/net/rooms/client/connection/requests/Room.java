package net.rooms.client.connection.requests;

import java.time.LocalDateTime;

public record Room(
		long roomID,
		String title,
		boolean isPrivate,
		String password,
		String owner,
		LocalDateTime creationDate,
		String description
) {
}