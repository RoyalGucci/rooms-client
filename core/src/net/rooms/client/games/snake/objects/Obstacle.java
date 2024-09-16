package net.rooms.client.games.snake.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.helper.Const;
import net.rooms.client.games.pong.helper.ContactType;
import net.rooms.client.games.snake.SnakeScreen;
import net.rooms.client.games.snake.helper.BodyHelperSnake;
import net.rooms.client.games.snake.helper.ContactInfo;
import net.rooms.client.games.snake.helper.headLocation;

import java.util.LinkedList;
import java.util.Queue;

public class Obstacle {
	private final Body body;
	private float x, y, velx, vely;
	private final float width, height, speed;
	private SnakeScreen gameScreen;
	private final Texture texture;

	public Obstacle(SnakeScreen gameScreen, float x, float y, float width, float height, ContactType contactType) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = 4;
		this.velx = 0;
		this.vely = 0;
		if(contactType == ContactType.OBSTACLE_HORIZONTAL)
			velx = 1;
		if(contactType == ContactType.OBSTACLE_VERTICAL)
			vely = 1;

		texture = new Texture("white.png");
		this.gameScreen = gameScreen;
		ContactInfo info = new ContactInfo(contactType, "");
		this.body = BodyHelperSnake.createBody(x, y, width, height, false, 0, gameScreen.getWorld(), info);
		this.body.setLinearVelocity(velx * speed, vely * speed);
	}
	public void update() {
		x = body.getPosition().x * Const.ppm - (width / 2);
		y = body.getPosition().y * Const.ppm - (height / 2);
	}

	public void updateVertical() {
		vely *= -1;
		body.setLinearVelocity(speed*velx, speed*vely);
	}

	public void updateHorizontal() {
		velx *= -1;
		body.setLinearVelocity(speed*velx, speed*vely);
	}

	public void update(Float locationX, Float locationY) {
		x = body.getPosition().x * Const.ppm - (width / 2);
		y = body.getPosition().y * Const.ppm - (height / 2);
		body.setLinearVelocity(locationX - x, locationY - y);
	}

	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y, width, height);
	}

	public Body getBody() {
		return body;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}


}
