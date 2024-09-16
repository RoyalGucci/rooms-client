package net.rooms.client.games.snake.objects;

import java.util.List;

public record GetSnake(
		List<Float> valuesX,
		List<Float> valuesY,
		String username
) {
}
