package net.rooms.client.games.snake;

import com.badlogic.gdx.physics.box2d.*;
import net.rooms.client.games.pong.helper.ContactType;
import net.rooms.client.games.snake.helper.ContactInfo;

public class SnakeContactListener  implements ContactListener {

	private final SnakeHostScreen gameScreen;

	public SnakeContactListener(SnakeHostScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();

		ContactInfo infoA = (ContactInfo) a.getUserData();
		ContactInfo infoB = (ContactInfo) b.getUserData();

		if(a.getUserData() == null || b.getUserData() == null)
			return;

		if(infoA.contactType() == ContactType.SNAKE_HEAD && infoB.contactType() == ContactType.FOOD){
			gameScreen.getSnake(infoA.username()).increase();
			gameScreen.getFood().setReset(true);
		}
		if(infoB.contactType() == ContactType.SNAKE_HEAD && infoA.contactType() == ContactType.FOOD){
			gameScreen.getSnake(infoB.username()).increase();
			gameScreen.getFood().setReset(true);
		}
		if(infoA.contactType() == ContactType.SNAKE_HEAD && (infoB.contactType() == ContactType.OBSTACLE_HORIZONTAL || infoB.contactType() == ContactType.OBSTACLE_VERTICAL)){
			gameScreen.getSnake(infoA.username()).setAlive(false);
		}
		if(infoB.contactType() == ContactType.SNAKE_HEAD && (infoA.contactType() == ContactType.OBSTACLE_HORIZONTAL || infoA.contactType() == ContactType.OBSTACLE_VERTICAL)){
			gameScreen.getSnake(infoB.username()).setAlive(false);
		}
		if(infoA.contactType() == ContactType.SNAKE_HEAD && (infoB.contactType() == ContactType.HORIZONTAL_WALL || infoB.contactType() == ContactType.VERTICAL_WALL)){
			gameScreen.getSnake(infoA.username()).setAlive(false);
		}
		if(infoB.contactType() == ContactType.SNAKE_HEAD && (infoA.contactType() == ContactType.HORIZONTAL_WALL || infoA.contactType() == ContactType.VERTICAL_WALL)){
			gameScreen.getSnake(infoB.username()).setAlive(false);
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
