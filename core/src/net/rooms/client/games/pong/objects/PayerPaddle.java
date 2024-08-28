package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.GameScreen;
import net.rooms.client.games.pong.helper.BodyHelper;
import net.rooms.client.games.pong.helper.Const;
import net.rooms.client.games.pong.helper.ContactType;

public abstract class PayerPaddle {

    protected Body body;
    protected float x, y, speed, velocityY;
    protected int width, height, score;
    protected Texture texture;
    protected GameScreen gameScreen;

    public PayerPaddle(float x, float y, GameScreen gameScreen) {
        this.x = x;
        this.y = y;
        this.gameScreen = gameScreen;
        this.speed = 6;
        this.width = 16;
        this.height = 64;
        this.texture = new Texture("white.png");
        this.body = BodyHelper.createBody(x, y, width,height, false, 10000, gameScreen.getWorld(), ContactType.PLAYER);
    }

    public void update() {
        x = body.getPosition().x * Const.ppm - (width / 2);
        y = body.getPosition().y * Const.ppm - (height / 2);
        velocityY = 0;

    }

    public void render(SpriteBatch batch){
        batch.draw(texture,x,y,width,height);
    }

    public void score(){
        this.score++;
    }
}
