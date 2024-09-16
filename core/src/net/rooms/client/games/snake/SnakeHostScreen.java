package net.rooms.client.games.snake;

import com.badlogic.gdx.Gdx;
import net.rooms.client.Client;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.GameUpdate;
import net.rooms.client.games.snake.objects.GetSnake;
import net.rooms.client.games.snake.objects.SendSnake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SnakeHostScreen extends SnakeScreen {

	private final Queue<GetSnake> moves;

	public SnakeHostScreen(Client client, GameUpdate update, long gameID, String participant, String host) {
		super(client, update, gameID, participant, host);
		moves = new LinkedList<>();
		world.setContactListener(new SnakeContactListener(this));
		channelListener.setHostChannelListener((getSnake -> Gdx.app.postRunnable(() -> SnakeHostScreen.this.onNetworkEvent(getSnake))), GetSnake.class);
	}

	public void onNetworkEvent(GetSnake getsnake) {
		if (getsnake == null) return;
		moves.add(getsnake);
	}

	@Override
	public void update() {
		super.update();
		food.update();
		getSnake(participant).update();
		moves.forEach(getSnake -> getSnake(getSnake.username()).update(getSnake.valuesX(), getSnake.valuesY()));
		moves.clear();

		ArrayList<SendSnake.SendSnakeEntry> snakeEntries = new ArrayList<>();
		ArrayList<SendSnake.SendFoodEntry> foodEntries = new ArrayList<>();
		getSnakes().forEach(snake -> snakeEntries.add(new SendSnake.SendSnakeEntry(snake.getUsername(), snake.locationX(), snake.locationY())));
		foodEntries.add(new SendSnake.SendFoodEntry(getFood().getX(),getFood().getY()));
		SendSnake sendSnake = new SendSnake(snakeEntries, foodEntries);
		client.getApiRequests().sendToGameGuests(gameID, JSON.toJson(sendSnake));
	}
}
