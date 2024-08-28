package net.rooms.client.games.pong.helper;

import com.badlogic.gdx.physics.box2d.*;

public class BodyHelper {

    public static Body createBody(float x, float y, float width, float height, boolean isStatic, float density, World world, ContactType type){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = !isStatic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / Const.ppm, y/ Const.ppm);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / Const.ppm, height / 2 / Const.ppm); // this defines the collision area

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        body.createFixture(fixtureDef).setUserData(type);

        shape.dispose();
        return body;
    }
}
