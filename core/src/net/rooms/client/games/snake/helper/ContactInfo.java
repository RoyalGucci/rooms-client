package net.rooms.client.games.snake.helper;

import net.rooms.client.games.pong.helper.ContactType;

public record ContactInfo(
		ContactType contactType,
		String username
) {
}
