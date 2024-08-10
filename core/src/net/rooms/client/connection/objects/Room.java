package net.rooms.client.connection.objects;

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