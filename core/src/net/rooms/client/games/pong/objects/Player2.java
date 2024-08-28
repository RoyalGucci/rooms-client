package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.rooms.client.games.pong.GameScreen;

public class Player2 extends PayerPaddle {

    public Player2(float x, float y, GameScreen gameScreen) {
        super(x, y, gameScreen);
    }

    public void update(){
        super.update();

        Ball ball = gameScreen.getBall();
        /*if(ball.getY() + 10 > y && ball.getY() - 10 > y)
            velocityY = 1;
        if(ball.getY() + 10 < y && ball.getY() - 10 < y)
            velocityY = -1;*/
        if (Gdx.input.isKeyPressed(Input.Keys.U))
            velocityY = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.J))
            velocityY = -1;

        body.setLinearVelocity(0, velocityY * speed);
    }
}
