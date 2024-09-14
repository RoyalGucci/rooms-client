package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.rooms.client.games.pong.PongScreen;
import net.rooms.client.games.pong.helper.ContactType;

public class Player extends PlayerPaddle {

	public Player(float x, float y, PongScreen pongScreen, String username, ContactType contactType) {
		super(x, y, pongScreen, contactType, username);
	}

	public void update() {
		super.update();
		if (!alive) return;

		if (Gdx.input.isKeyPressed(Input.Keys.W) && contactType == ContactType.VERTICAL_PLAYER) {
			velocityY = 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) && contactType == ContactType.VERTICAL_PLAYER) {
			velocityY = -1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) && contactType == ContactType.HORIZONTAL_PLAYER) {
			velocityX = 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A) && contactType == ContactType.HORIZONTAL_PLAYER) {
			velocityX = -1;
		}
		if (contactType == ContactType.VERTICAL_PLAYER)
			body.setLinearVelocity(0, velocityY * speed);
		if (contactType == ContactType.HORIZONTAL_PLAYER)
			body.setLinearVelocity(velocityX * speed, 0);
	}

	public void update(float location) {
		super.update();
		if (!alive || pongScreen.participant.equals(username)) return;

		if (contactType == ContactType.VERTICAL_PLAYER) {
			body.setLinearVelocity(0,  location - y);
		}
		else if (contactType == ContactType.HORIZONTAL_PLAYER) {
			body.setLinearVelocity(location - x, 0);
		}

	}
}
