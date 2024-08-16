package net.rooms.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import net.rooms.client.connection.APIRequests;


public class Client extends Game {

	private APIRequests apiRequests;
	private ScreenManager screenManager;
	private Repository repository;
	private SpriteBatch batch;
	private BitmapFont font;

	@Override
	public void create() {
		VisUI.load();
		apiRequests = new APIRequests();
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(0, 0, 0, 1);
		screenManager = new ScreenManager(this);
		screenManager.login();
		repository = new Repository();
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.end();
		super.render();
	}

	@Override
	public void dispose() {
		screenManager.dispose();
		batch.dispose();
		font.dispose();
	}

	public APIRequests getApiRequests() {
		return apiRequests;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public Repository getRepository() { // TODO Use everywhere to store and access user data
		return repository;
	}
}
