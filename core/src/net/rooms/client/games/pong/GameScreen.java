package net.rooms.client.games.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import net.rooms.client.games.pong.helper.Const;
import net.rooms.client.games.pong.objects.*;

public class GameScreen extends ScreenAdapter {

	private OrthographicCamera camera; //where do we look at
	private SpriteBatch batch; //add things like images
	private World world; //used for the gravity
	private Box2DDebugRenderer box2DDebugRenderer;

	private Player player;
	private Player2 player2;
	private Ball ball;
	private Wall wallTop, wallbuttom;
	private VerticalWall deathwall;

	private GameContactListener gameContactListener;

	private int playerNumber;

	public GameScreen(OrthographicCamera camera, int playerNumber) {
		this.playerNumber = playerNumber;
		this.camera = camera;
		this.camera.position.set(new Vector3(PongGame.INSTANCE.getScreenwidth() / 2,PongGame.INSTANCE.getScreenheight() / 2,0));
		this.batch = new SpriteBatch();
		this.world = new World(new Vector2(0,0),false);
		this.box2DDebugRenderer = new Box2DDebugRenderer();

		this.player = new Player(16, PongGame.INSTANCE.getScreenheight() / 2, this, 1);
		this.player2 = new Player2(PongGame.INSTANCE.getScreenwidth() - 16,PongGame.INSTANCE.getScreenheight() / 2, this);
		this.ball = new Ball(this);
		this.wallTop = new Wall(0, this);
		this.wallbuttom = new Wall(PongGame.INSTANCE.getScreenheight(), this);

		this.deathwall = new VerticalWall(PongGame.INSTANCE.getScreenheight() / 2 + 200 , this);
		this.gameContactListener = new GameContactListener(this);
		this.world.setContactListener(this.gameContactListener);
	}

	public void update(){
		world.step(1/60f,6,2); //how many updates per second

		this.camera.update();
		this.player.update();
		this.player2.update();
		this.ball.update();

		batch.setProjectionMatrix(camera.combined);

		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
			this.ball.reset();
		}
	}

	public void render(float delta){
		update();
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		//inside we can draw shapes with batch
		this.player.render(batch);
		this.player2.render(batch);
		this.ball.render(batch);
		this.wallTop.render(batch);
		this.wallbuttom.render(batch);
		this.deathwall.render(batch);
		batch.end();

		//this.box2DDebugRenderer.render(world, camera.combined.scl(Const.ppm));
	}

	public World getWorld() {
		return world;
	}

	public Ball getBall() {
		return ball;
	}

	public Player2 getPlayer2() {
		return player2;
	}

	public Player getPlayer() {
		return player;
	}
}