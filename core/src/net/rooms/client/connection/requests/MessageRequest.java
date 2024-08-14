package net.rooms.client.connection.requests;

import net.rooms.client.connection.objects.MessageType;

public record MessageRequest(
		long roomID,
		MessageType type,
		String content,
		String jSessionID
) {
}
