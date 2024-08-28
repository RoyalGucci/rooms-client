package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.GameScreen;
import net.rooms.client.games.pong.PongGame;
import net.rooms.client.games.pong.helper.BodyHelper;
import net.rooms.client.games.pong.helper.ContactType;

public class Wall {
    private Body body;
    private float x,y;
    private int width, height;
    private Texture texture;

    public Wall(float y, GameScreen gameScreen){
        this.x = PongGame.INSTANCE.getScreenwidth() / 2;
        this.y = y;
        this.width = PongGame.INSTANCE.getScreenwidth();
        this.height = 16;

        this.texture = new Texture("white.png");
        this.body = BodyHelper.createBody(x,y,width,height,true,0,gameScreen.getWorld(), ContactType.WALL);
    }

    public void render(SpriteBatch batch){
        batch.draw(texture,x - (width / 2),y - (height / 2),width,height);
    }
}
