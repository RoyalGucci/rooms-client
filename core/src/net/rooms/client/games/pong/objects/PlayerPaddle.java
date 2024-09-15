package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.PongScreen;
import net.rooms.client.games.pong.helper.BodyHelper;
import net.rooms.client.games.pong.helper.ColorTypes;
import net.rooms.client.games.pong.helper.Const;
import net.rooms.client.games.pong.helper.ContactType;

public abstract class PlayerPaddle {

	protected Body body;
	protected float x, y, speed, velocityY, velocityX;
	protected int width, height, score;
	protected Texture texture;
	protected PongScreen pongScreen;
	protected final String username;
	ContactType contactType;
	protected boolean alive;

	private PlayerPaddle(float x, float y, ContactType contactType, String username, ColorTypes colorType) {
		this.x = x;
		this.y = y;
		this.speed = 10;
		score = 0;
		if (contactType == ContactType.HORIZONTAL_PLAYER) {
			this.width = 64;
			this.height = 16;
		}
		else{
			this.width = 16;
			this.height = 64;
		}
		switch (colorType) {
			case RED-> texture = new Texture("red.png");
			case BLUE-> texture = new Texture("blue.png");
			case GREEN-> texture = new Texture("green.png");
			case GOLD -> texture = new Texture("gold.png");
		}

		this.username = username;
		this.contactType = contactType;
		alive = false;
	}

	public PlayerPaddle(float x, float y, PongScreen pongScreen, ContactType contactType, String username, ColorTypes colorType) {
		this(x, y, contactType, username, colorType);
		this.pongScreen = pongScreen;
		body = BodyHelper.createBody(this.x, this.y, this.width, this.height, false, 10000, pongScreen.getWorld(), contactType);
	}

	public void update() {
		if (!alive) return;
		x = body.getPosition().x * Const.ppm - ((float) width / 2);
		y = body.getPosition().y * Const.ppm - ((float) height / 2);
		velocityY = 0;
		velocityX = 0;
	}

	public void render(SpriteBatch batch) {
		if (!alive) return;
		batch.draw(texture, x, y, width, height);
	}

	public void score() {
		if (!alive) return;
		score++;
		if (score == pongScreen.winScore)
			pongScreen.winner(username);
	}

	public void setActive(boolean active) {
		body.setActive(active);
	}

	public float getLocation() {
		if (contactType == ContactType.HORIZONTAL_PLAYER) return x;
		return y;
	}

	public String getUsername() {
		return username;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
