package net.rooms.client.games.pong;

import com.badlogic.gdx.physics.box2d.*;
import net.rooms.client.games.pong.helper.ContactType;

public class GameContactListener  implements ContactListener {

	private final PongHostScreen gameScreen;

	public GameContactListener(PongHostScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();

		if(a == null || b == null)
			return;
		if(a.getUserData() == null || b.getUserData() == null)
			return;
		if(a.getUserData() == ContactType.BALL && (b.getUserData() == ContactType.VERTICAL_PLAYER || b.getUserData() == ContactType.VERTICAL_WALL)){
			gameScreen.getBall().reversevelX();
			gameScreen.getBall().incspeed();
		}
		if(b.getUserData() == ContactType.BALL && (a.getUserData() == ContactType.VERTICAL_PLAYER || a.getUserData() == ContactType.VERTICAL_WALL)){
			gameScreen.getBall().reversevelX();
			gameScreen.getBall().incspeed();
		}
		if(a.getUserData() == ContactType.BALL && (b.getUserData() == ContactType.HORIZONTAL_WALL || b.getUserData() == ContactType.HORIZONTAL_PLAYER)){
			gameScreen.getBall().reversevelY();
		}
		if(b.getUserData() == ContactType.BALL && (a.getUserData() == ContactType.HORIZONTAL_WALL || a.getUserData() == ContactType.HORIZONTAL_PLAYER)){
			gameScreen.getBall().reversevelY();
		}
	}


	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
