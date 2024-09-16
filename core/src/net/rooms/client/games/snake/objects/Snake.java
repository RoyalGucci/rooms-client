package net.rooms.client.games.snake.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.rooms.client.games.snake.SnakeScreen;
import net.rooms.client.games.snake.helper.RotationHelper;
import net.rooms.client.games.snake.helper.headLocation;

import java.util.ArrayList;
import java.util.List;

public class Snake {
	private final SnakeHead head;
	private int score;
	private final SnakeScreen gameScreen;
	protected final String username;
	private final boolean alive;

	public Snake(SnakeScreen gameScreen, float x, float y, String username) {
		this.gameScreen = gameScreen;
		this.score = 0;
		this.username = username;
		this.alive = true;
		this.head = new SnakeHead(gameScreen, x, y,30,30, username);
	}

	public void update() {
		RotationHelper rotationHelper = RotationHelper.ZERO;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			rotationHelper = RotationHelper.NEGATIVE;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			rotationHelper = RotationHelper.POSITIVE;
		}
		headLocation location = head.update(rotationHelper);
	}

	public void update(List<Float> locationX, List<Float> locationY) {
		head.update(locationX.getFirst(), locationY.getFirst());
	}

	public void render(SpriteBatch batch) {
		if(!alive) {

			return;
		}
		head.render(batch);
	}

	public void setActive(boolean active) {
		head.setActive(active);
	}

	public void setAlive(boolean alive) {
		head.setAlive(alive);
	}

	public String getUsername() {
		return username;
	}

	public void increase(){
		score++;
	}

	public int getScore() {
		return score;
	}

	public List<Float> locationX() {
		List<Float> valuesX = new ArrayList<>();
		valuesX.add(head.getX());
		return valuesX;
	}

	public List<Float> locationY() {
		List<Float> valuesY = new ArrayList<>();
		valuesY.add(head.getY());
		return valuesY;
	}


}
