package net.rooms.client.games.snake;

import com.badlogic.gdx.Gdx;
import net.rooms.client.Client;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.GameUpdate;
import net.rooms.client.games.snake.objects.GetSnake;
import net.rooms.client.games.snake.objects.SendSnake;
import net.rooms.client.games.snake.objects.Snake;

public class SnakeGuestScreen extends SnakeScreen {

	private volatile SendSnake lastUpdate;

	public SnakeGuestScreen(Client client, GameUpdate update, long gameID, String participant, String host) {
		super(client, update, gameID, participant, host);
		channelListener.setGuestChannelListener((sendSnake -> Gdx.app.postRunnable(() -> SnakeGuestScreen.this.onNetworkEvent(sendSnake))), SendSnake.class);
	}

	public void onNetworkEvent(SendSnake sendSnake) {
		lastUpdate = sendSnake;
	}

	@Override
	public void update() {
		super.update();
		if (lastUpdate != null) {
			lastUpdate.players().forEach(entry -> getSnake(entry.username()).update(entry.valuesX(),entry.valuesY()));
			lastUpdate.food().forEach(entry -> getFood().update(entry.locationX(),entry.locationY()));
		}
		Snake snake = getSnake(participant);
		snake.update();
		GetSnake getSnake = new GetSnake(snake.locationX(),snake.locationY(),snake.getUsername());
		client.getApiRequests().sendToGameHost(gameID, JSON.toJson(getSnake));
	}
}
