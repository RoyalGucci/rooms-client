package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.PongScreen;
import net.rooms.client.games.pong.helper.BodyHelper;
import net.rooms.client.games.pong.helper.ContactType;

public class Wall {
	private final Body body;
	private final float x, y;
	private final int width, height;
	private final Texture texture;
	private ContactType contactType;

	public Wall(float location, PongScreen gameScreen, Player player, WallSide wallSide) {
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
		body = BodyHelper.createBody(x, y, width, height, true, 0, gameScreen.getWorld(), contactType);
		body.setActive(!player.alive);
	}

	public void setActive(boolean active) {
		body.setActive(active);
	}

	public void render(SpriteBatch batch) {
		if (!body.isActive()) return;
		batch.draw(texture, x - ((float) width / 2), y - ((float) height / 2), width, height);
	}

	public enum WallSide {
		LEFT, RIGHT, TOP, BOTTOM
	}
}
