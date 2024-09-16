package net.rooms.client.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.rooms.client.Client;
import net.rooms.client.connection.objects.GameUpdate;
import net.rooms.client.connection.objects.SnakesConfig;
import net.rooms.client.games.GameScreen;
import net.rooms.client.games.snake.objects.Food;
import net.rooms.client.games.snake.objects.Snake;
import net.rooms.client.games.snake.objects.WallSnake;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class SnakeScreen extends GameScreen {

	private final Viewport viewport;
	public static final int PONG_SCREEN_SIZE = 800;
	public final String participant;
	private final String host;
	public final int winScore;
	protected int playerNumber;
	protected final HashMap<String, Snake> snakes;
	protected final HashMap<WallSnake.WallSide, WallSnake> walls;
	protected Food food;
	protected final Snake empty;
	protected final SnakesConfig config;
	protected final List<String> participants;

	public SnakeScreen(Client client, GameUpdate update, long gameID, String participant, String host) {
		super(client, update, gameID);
		this.participant = participant;
		this.host = host;
		config = (SnakesConfig) update.config();
		winScore = config.winScore();
		empty = new Snake(this, 400, 400, "");
		empty.setActive(false);
		viewport = new FitViewport(PONG_SCREEN_SIZE, PONG_SCREEN_SIZE);

		playerNumber = update.participants().size();
		snakes = new HashMap<>(playerNumber);
		float[][] sides = new float[][]{{(float) PONG_SCREEN_SIZE / 4, (float) (PONG_SCREEN_SIZE / 4) * 3},
				{(float) PONG_SCREEN_SIZE / 4, (float) PONG_SCREEN_SIZE / 4},
				{(float) (PONG_SCREEN_SIZE / 4) * 3, (float) PONG_SCREEN_SIZE / 4},
				{(float) (PONG_SCREEN_SIZE / 4) * 3, (float) (PONG_SCREEN_SIZE / 4) * 3}};
		WallSnake.WallSide[] wallSides = new WallSnake.WallSide[]{WallSnake.WallSide.LEFT, WallSnake.WallSide.RIGHT, WallSnake.WallSide.BOTTOM, WallSnake.WallSide.TOP};
		participants = update.participants();
		for (int i = 0; i < participants.size(); i++) {
			String username = participants.get(i);
			Snake snake = new Snake(this, sides[i][0], sides[i][1], username);
			snakes.put(username, snake);
			snake.setAlive(true);
		}

		walls = new HashMap<>();
		int[] wallLocations = new int[]{0, PONG_SCREEN_SIZE, 0, PONG_SCREEN_SIZE};
		for (int i = 0; i < 4; i++) {
			walls.put(wallSides[i], new WallSnake(wallLocations[i], this, wallSides[i]));
			System.out.println(wallSides[i] + " " + wallLocations[i]);
		}
		food = new Food(this);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true); // Maintain aspect ratio
	}

	@Override
	public void update() {
		super.update();
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) client.getApiRequests().leaveGame(gameID);
	}

	private void quit() {
		client.getScreenManager().dashboard();
		dispose();
	}

	@Override
	public void render(float delta) {
		update();
		super.render();
		viewport.apply();

		batch.begin();
		food.render(batch);
		snakes.values().forEach(snake -> snake.render(batch));
		walls.values().forEach(wall -> wall.render(batch));
		batch.end();
	}


	@Override
	public void onDisconnect(String username) {
		playerNumber--;
		if (!host.equals(username) && playerNumber <= 1)
			client.getApiRequests().leaveGame(gameID); // Host must close the game
		if (participant.equals(username) || host.equals(username) || playerNumber <= 1) {
			quit();
			return;
		}
		Snake snake = getSnake(username);
		snake.setAlive(false);
		snake.setActive(false);
		snakes.remove(username);
	}

	public Snake getSnake(String username) {
		return snakes.getOrDefault(username, empty);
	}

	public Food getFood() {
		return food;
	}

	public Collection<Snake> getSnakes() {
		return snakes.values();
	}
}
