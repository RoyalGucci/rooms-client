package net.rooms.client.connection.objects;

import java.time.LocalDateTime;

public record Participant(
		long roomID,
		String nickname,
		String username,
		LocalDateTime signupDate
) {
}
