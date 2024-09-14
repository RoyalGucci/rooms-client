package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.PongScreen;
import net.rooms.client.games.pong.helper.BodyHelper;
import net.rooms.client.games.pong.helper.Const;
import net.rooms.client.games.pong.helper.ContactType;

import static net.rooms.client.games.pong.PongScreen.PONG_SCREEN_SIZE;
import static net.rooms.client.games.pong.objects.Wall.WallSide.*;

public class Ball {

	private final Body body;
	private float x, y, speed, velx, vely;
	private final int width, height;
	private final PongScreen pongScreen;
	private final Texture texture;
	private int dead;


	public Ball(PongScreen gameScreen) {
		x = (float) PONG_SCREEN_SIZE / 2;
		y = (float) PONG_SCREEN_SIZE / 2;
		speed = 2;
		dead = 0;

		texture = new Texture("white.png");
		pongScreen = gameScreen;
		height = 32;
		width = 32;
		body = BodyHelper.createBody(x, y, width, height, false, 0, pongScreen.getWorld(), ContactType.BALL);
		reset();
	}

	private float getRandomDirection() {
		return (float) ((Math.random() < 0.5) ? Math.random() * 0.5 - 1 : Math.random() * 0.5 + 0.5);
	}

	public void update(boolean host, float xlocation, float ylocation) {
		if (host) {
			body.setLinearVelocity(velx * speed, vely * speed);
			x = body.getPosition().x * Const.ppm - ((float) width / 2);
			y = body.getPosition().y * Const.ppm - ((float) height / 2);
		} else {
			body.setTransform(xlocation, ylocation, 0);
			x = xlocation;
			y = ylocation;
		}

		if (x < 0) disqualify(LEFT);
		if (x > PONG_SCREEN_SIZE) disqualify(RIGHT);
		if (y < 0) disqualify(BOTTOM);
		if (y > PONG_SCREEN_SIZE) disqualify(TOP);
	}

	private void disqualify(Wall.WallSide side) {
		pongScreen.getPlayer(side).setAlive(false);
		pongScreen.getPlayer(side).setActive(false);
		pongScreen.getWall(side).setActive(true);
		score(side);
		reset();
	}

	public void score(Wall.WallSide lost) {
		dead++;
		Wall.WallSide[] wallSides = new Wall.WallSide[]{Wall.WallSide.LEFT, Wall.WallSide.RIGHT, Wall.WallSide.BOTTOM, Wall.WallSide.TOP};
		for (Wall.WallSide wallSide : wallSides)
			if (wallSide != lost)
				pongScreen.getPlayer(wallSide).score();
		if (dead >= pongScreen.getPlayerNumber() - 1) pongScreen.reset();

	}

	public void reset() {
		velx = getRandomDirection();
		vely = getRandomDirection();
		speed = 4;
		body.setTransform((float) PONG_SCREEN_SIZE / 2 / Const.ppm, (float) PONG_SCREEN_SIZE / 2 / Const.ppm, 0);
	}

	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y, width, height);
	}

	public void reversevelX() {
		velx *= -1;
	}

	public void reversevelY() {
		vely *= -1;
	}

	public void incspeed() {
		speed *= 1.1f;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setDead() {
		dead = 0;
	}

	public void setActive(boolean active) {
		body.setActive(active);
	}
}
