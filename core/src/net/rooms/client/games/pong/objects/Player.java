package net.rooms.client.games.pong.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.rooms.client.games.pong.GameScreen;

public class Player extends PayerPaddle{
    private int number;

    public Player(float x, float y , GameScreen gameScreen, int number){
        super(x,y,gameScreen);
        number = number;
    }

    public void update(){
        super.update();
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            velocityY = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            velocityY = -1;
        /*if(number == 1) {
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                velocityY = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S))
                velocityY = -1;
        }
        else if(number == 2) {
            if (Gdx.input.isKeyPressed(Input.Keys.U))
                velocityY = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.J))
                velocityY = -1;
        }*/

        body.setLinearVelocity(0,velocityY * speed);
    }
}
