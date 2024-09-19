package net.rooms.client.connection.objects;

import java.time.LocalDateTime;

public record Message(
		long id,
		long roomID, // The room to which the message is for
		MessageType type,
		String sender, // Username of the sender of this message
		String content,
		LocalDateTime sendDate
) {
}
