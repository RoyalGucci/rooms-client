package net.rooms.client.connection.objects;

import java.util.List;

public record GameUpdate(
		GameConfig config,
		String username,
		List<String> participants
) {
}
