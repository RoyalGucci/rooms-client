package net.rooms.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import net.rooms.client.connection.APIRequests;
import net.rooms.client.ui.login.LoginScreen;
import net.rooms.client.ui.signup.SignupScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Client extends Game {

	public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 1000;

	private APIRequests apiRequests;
	private ScreenManager screenManager;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;

	@Override
	public void create () {
		VisUI.load();
		apiRequests = new APIRequests();
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(0, 0, 0, 1);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		screenManager = new ScreenManager(this);
		screenManager.login();

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.end();
		super.render();
	}

	@Override
	public void dispose () {
		//screenManager.dispose();
		batch.dispose();
		font.dispose();
	}

	public APIRequests getApiRequests() {
		return apiRequests;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
