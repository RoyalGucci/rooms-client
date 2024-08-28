package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import net.rooms.client.games.pong.GameScreen;
import net.rooms.client.games.pong.PongGame;
import net.rooms.client.games.pong.helper.BodyHelper;
import net.rooms.client.games.pong.helper.Const;
import net.rooms.client.games.pong.helper.ContactType;

public class Ball {

    private Body body;
    private float x, y, speed, velx, vely;
    private int width, height;
    private GameScreen gameScreen;
    private Texture texture;

    public Ball(GameScreen gameScreen){
        this.x = PongGame.INSTANCE.getScreenwidth() / 2;
        this.y = PongGame.INSTANCE.getScreenheight() / 2;
        this.speed = 5;
        this.velx = getRandomDirection();
        this.vely = getRandomDirection();

        this.texture = new Texture("white.png");
        this.gameScreen = gameScreen;
        this.height = 32;
        this.width = 32;
        this.body = BodyHelper.createBody(x,y,width,height,false,0,gameScreen.getWorld(), ContactType.BALL);
    }

    private float getRandomDirection(){
        return (Math.random() < 0.5) ? 1 : -1;
    }

    public void update() {
        x = body.getPosition().x * Const.ppm - (width / 2);
        y = body.getPosition().y * Const.ppm - (height / 2);

        this.body.setLinearVelocity(velx * speed, vely * speed);

        if(x < 0){
            gameScreen.getPlayer2().score();
            reset();
        }

        if(x > PongGame.INSTANCE.getScreenwidth()){
            gameScreen.getPlayer().score();
            reset();
        }

    }

    public void reset(){
        this.velx = this.getRandomDirection();
        this.vely = this.getRandomDirection();
        this.speed = 5;
        this.body.setTransform(PongGame.INSTANCE.getScreenwidth() / 2 /Const.ppm, PongGame.INSTANCE.getScreenheight() / 2 /Const.ppm,0);
    }

    public void render(SpriteBatch batch){
        batch.draw(texture,x,y,width,height);
    }

    public void reversevelX(){
        this.velx *= -1;
    }

    public void reversevelY(){
        this.vely *= -1;
    }

    public void incspeed(){
        this.speed *= 1.1f;
    }

    public float getY() {
        return y;
    }
}
