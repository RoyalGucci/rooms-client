package net.rooms.client.connection.objects;

import java.time.LocalDateTime;

public record PublicRoom(
		long roomID,
		String title,
		boolean hasPassword,
		String owner,
		LocalDateTime creationDate,
		String description
) {
}
