package net.rooms.client.games.snake.helper;

import net.rooms.client.games.pong.helper.ContactType;
import net.rooms.client.games.snake.objects.SnakeBody;
import net.rooms.client.games.snake.objects.SnakeHead;

public record ContactInfo(
		ContactType contactType,
		SnakeHead snakeHead,
		SnakeBody snakeBody
) {
}
