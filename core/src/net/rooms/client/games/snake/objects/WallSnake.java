package net.rooms.client.games.snake.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.PongScreen;
import net.rooms.client.games.pong.helper.ContactType;
import net.rooms.client.games.snake.SnakeScreen;
import net.rooms.client.games.snake.helper.BodyHelperSnake;
import net.rooms.client.games.snake.helper.ContactInfo;

public class WallSnake {
	private final Body body;
	private final float x, y;
	private final int width, height;
	private final Texture texture;
	private ContactType contactType;

	public WallSnake(float location, SnakeScreen gameScreen, WallSnake.WallSide wallSide) {
		switch (wallSide) {
			case LEFT, RIGHT -> {
				x = location;
				y = (float) PongScreen.PONG_SCREEN_SIZE / 2;
				width = 16;
				height = PongScreen.PONG_SCREEN_SIZE;
				contactType = ContactType.VERTICAL_WALL;
			}
			case TOP, BOTTOM -> {
				x = (float) PongScreen.PONG_SCREEN_SIZE / 2;
				y = location;
				width = PongScreen.PONG_SCREEN_SIZE;
				height = 16;
				contactType = ContactType.HORIZONTAL_WALL;
			}
			default -> {
				x = 0;
				y = 0;
				width = 0;
				height = 0;
			}
		}

		texture = new Texture("white.png");
		ContactInfo info = new ContactInfo(contactType, "");
		body = BodyHelperSnake.createBody(x, y, width, height,true, 0, gameScreen.getWorld(), info);
	}

	public void render(SpriteBatch batch) {
		batch.draw(texture, x - ((float) width / 2), y - ((float) height / 2), width, height);
	}

	public enum WallSide {
		LEFT, RIGHT, TOP, BOTTOM
	}
}
