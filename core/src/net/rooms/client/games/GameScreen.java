package net.rooms.client.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import net.rooms.client.Client;
import net.rooms.client.connection.objects.GameUpdate;

public abstract class GameScreen extends ScreenAdapter {
	protected final Client client;
	protected GameUpdate update;
	public final long gameID;
	protected final GameChannelListener channelListener;
	protected final OrthographicCamera camera; //where do we look at
	protected final SpriteBatch batch; //add things like images
	protected final World world; //used for the gravity

	public GameScreen(Client client, GameUpdate update, long gameID) {
		this.client = client;
		this.gameID = gameID;
		this.update = update;

		channelListener = new GameChannelListener(client);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(new Vector3((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 0));
		batch = new SpriteBatch();
		world = new World(new Vector2(0, 0), false);
	}

	protected void update() {
		world.step(1 / 60f, 6, 2); // 60 frames per second
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	protected void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public abstract void onDisconnect(String username);

	public World getWorld() {
		return world;
	}
}
