package net.rooms.client.games.pong;

import com.badlogic.gdx.physics.box2d.*;
import net.rooms.client.games.pong.helper.ContactType;

public class GameContactListener  implements ContactListener {

	private GameScreen gameScreen;

	public GameContactListener(GameScreen gameScreen) {
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
		if(a.getUserData() == ContactType.BALL && (b.getUserData() == ContactType.PLAYER || b.getUserData() == ContactType.VERTICALWALL)){
			gameScreen.getBall().reversevelX();
			gameScreen.getBall().incspeed();
		}
		if(b.getUserData() == ContactType.BALL && (a.getUserData() == ContactType.PLAYER || a.getUserData() == ContactType.VERTICALWALL)){
			gameScreen.getBall().reversevelX();
			gameScreen.getBall().incspeed();
		}
		if(a.getUserData() == ContactType.BALL && b.getUserData() == ContactType.WALL){
			gameScreen.getBall().reversevelY();
		}
		if(b.getUserData() == ContactType.BALL && a.getUserData() == ContactType.WALL){
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
