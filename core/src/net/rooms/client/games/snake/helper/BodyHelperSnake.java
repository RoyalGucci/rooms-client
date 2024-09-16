package net.rooms.client.games.snake.helper;

import com.badlogic.gdx.physics.box2d.*;
import net.rooms.client.games.pong.helper.Const;

public class BodyHelperSnake {

	public static Body createBody(float x, float y, float width, float height, boolean isStatic, float density, World world, ContactInfo info){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = !isStatic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
		bodyDef.position.set(x / Const.ppm, y/ Const.ppm);
		bodyDef.fixedRotation = true;
		Body body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 / Const.ppm, height / 2 / Const.ppm); // food

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		body.createFixture(fixtureDef).setUserData(info);

		shape.dispose();
		return body;
	}
}
