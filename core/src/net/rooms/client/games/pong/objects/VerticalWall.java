package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.GameScreen;
import net.rooms.client.games.pong.PongGame;
import net.rooms.client.games.pong.helper.BodyHelper;
import net.rooms.client.games.pong.helper.ContactType;

public class VerticalWall {
	private Body body;
	private float x,y;
	private int width, height;
	private Texture texture;

	public VerticalWall(float x, GameScreen gameScreen){
		this.x = x;;
		this.y = PongGame.INSTANCE.getScreenheight() / 2;
		this.width = 16;
		this.height = PongGame.INSTANCE.getScreenheight();

		this.texture = new Texture("white.png");
		this.body = BodyHelper.createBody(x,y,width,height,true,0,gameScreen.getWorld(), ContactType.VERTICALWALL);
	}

	public void render(SpriteBatch batch){
		batch.draw(texture,x - (width / 2),y - (height / 2),width,height);
	}
}
