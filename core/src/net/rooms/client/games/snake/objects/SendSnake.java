package net.rooms.client.games.snake.objects;

import java.util.List;

public record SendSnake (
		List<SendSnakeEntry> players,
		List<SendFoodEntry> food
){
	public record SendFoodEntry(
			float locationX,
			float locationY
	){
	}

	public record SendSnakeEntry(
			String username,
			List<Float> valuesX,
			List<Float> valuesY
	){
	}
}

