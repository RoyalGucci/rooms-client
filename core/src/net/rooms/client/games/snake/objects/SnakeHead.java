package net.rooms.client.games.snake.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.helper.Const;
import net.rooms.client.games.pong.helper.ContactType;
import net.rooms.client.games.snake.SnakeScreen;
import net.rooms.client.games.snake.helper.BodyHelperSnake;
import net.rooms.client.games.snake.helper.ContactInfo;
import net.rooms.client.games.snake.helper.RotationHelper;
import net.rooms.client.games.snake.helper.headLocation;

public class SnakeHead{
	private final Body body;
	private float x, y, rotationSpeed, totalRotation;
	private final float width,height, speed, velx, vely;
	private SnakeScreen gameScreen;
	private final Texture texture;
	private boolean alive;
	private String username;

	public SnakeHead(SnakeScreen gameScreen, float x, float y, float width, float height, String username) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = 4;
		this.velx = 1;
		this.vely = 0;
		this.rotationSpeed = 0;
		this.username = username;
		alive = true;

		this.texture = new Texture("white.png");
		this.gameScreen = gameScreen;
		ContactInfo info = new ContactInfo(ContactType.SNAKE_HEAD, username);
		this.body = BodyHelperSnake.createBody(x, y, width, height, false, 0, gameScreen.getWorld(), info);
		this.body.setLinearVelocity(velx*speed, vely*speed);
	}

	public headLocation update(RotationHelper rotationHelper) {
		switch (rotationHelper) {
			case NEGATIVE -> rotationSpeed = -0.05f;
			case POSITIVE -> rotationSpeed = 0.05f;
			case ZERO -> rotationSpeed = 0;
		}
		x = body.getPosition().x * Const.ppm - (width / 2);
		y = body.getPosition().y * Const.ppm - (height / 2);
		totalRotation += rotationSpeed;
		Vector2 vector2 = getComponentsFromAngle(totalRotation);
		body.setLinearVelocity(vector2.x, vector2.y);
		body.setAngularVelocity(rotationSpeed);
		return new headLocation(x,y,rotationSpeed,vector2.x, vector2.y);
	}

	public void update(Float locationX, Float locationY) {
		x = body.getPosition().x * Const.ppm - (width / 2);
		y = body.getPosition().y * Const.ppm - (height / 2);
		body.setLinearVelocity(locationX - x, locationY - y);
	}

	public void render(SpriteBatch batch) {
		if(!alive) {
			setActive(false);
			return;
		}
		batch.draw(texture, x, y,width,height);
	}

	public void setActive(boolean active) {
		body.setActive(active);
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Body getBody() {
		return body;
	}

	public float getX() {
		return x;
	}

	public Vector2 getComponentsFromAngle(float angleRad) {
		float x = (float) Math.cos(angleRad);
		float y = (float) Math.sin(angleRad);
		return new Vector2(x, y);
	}

	public float getY() {
		return y;
	}


}
