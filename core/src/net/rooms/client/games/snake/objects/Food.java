package net.rooms.client.games.snake.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.helper.Const;
import net.rooms.client.games.pong.helper.ContactType;
import net.rooms.client.games.snake.SnakeScreen;
import net.rooms.client.games.snake.helper.BodyHelperSnake;
import net.rooms.client.games.snake.helper.ContactInfo;

import java.util.Random;

import static net.rooms.client.games.pong.PongScreen.PONG_SCREEN_SIZE;

public class Food {
	private final Body body;
	private float x, y;
	private final int width, height;
	private SnakeScreen snakeScreen;
	private final Texture texture;
	private boolean reset;

	public Food(SnakeScreen gameScreen) {
		this.x = MathUtils.random( 50, Gdx.graphics.getWidth() - 50);
		this.y = MathUtils.random( 50, Gdx.graphics.getHeight() - 50);

		this.texture = new Texture("white.png");
		this.snakeScreen = gameScreen;
		this.height = 10;
		this.width = 10;
		this.reset = false;
		ContactInfo info = new ContactInfo(ContactType.FOOD, "");
		this.body = BodyHelperSnake.createBody(x, y, width, height, false, 0, gameScreen.getWorld(), info);
	}

	public void render(SpriteBatch batch) {
		if(reset){
			reset();
			reset = false;
		}
		batch.draw(texture, x, y, width, height);
	}

	public void reset() {
		Random random = new Random();
		int x = random.nextInt(20 , PONG_SCREEN_SIZE - 20);
		int y = random.nextInt(20 , PONG_SCREEN_SIZE - 20);
		body.setTransform(x / Const.ppm, y / Const.ppm, 0);
	}
	public float getX() {
		return x;
	}

	public void setReset(boolean reset){
		this.reset = reset;
	}

	public float getY() {
		return y;
	}

	public void update(){
		x = body.getPosition().x * Const.ppm - ((float) width / 2);
		y = body.getPosition().y * Const.ppm - ((float) height / 2);
	}

	public void update(Float locationX, Float locationY) {
		x = body.getPosition().x * Const.ppm - ((float) width / 2);
		y = body.getPosition().y * Const.ppm - ((float) height / 2);
		body.setLinearVelocity(locationX - x, locationY - y);
	}
}
