package net.rooms.client.games.pong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PongGame extends Game { //using game to allow multiple screens
	public static PongGame INSTANCE;
	private int screenwidth, screenheight;
	private OrthographicCamera orthographicCamera;

	public PongGame() {
		INSTANCE = this;
	}

	@Override
	public void create() {
		this.screenheight = Gdx.graphics.getHeight();
		this.screenwidth = Gdx.graphics.getWidth();
		this.orthographicCamera = new OrthographicCamera();
		this.orthographicCamera.setToOrtho(false, screenwidth,screenheight);
		setScreen(new GameScreen(orthographicCamera, 2));

	}

	public int getScreenwidth() {
		return screenwidth;
	}

	public int getScreenheight() {
		return screenheight;
	}


}