package net.rooms.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import net.rooms.client.connection.APIRequests;


public class Client extends Game {

	private APIRequests apiRequests;
	private ScreenManager screenManager;
	private Repository repository;

	@Override
	public void create() {
		apiRequests = new APIRequests();
		screenManager = new ScreenManager(this);
		screenManager.login();
		repository = new Repository();
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
		super.render();
	}

	@Override
	public void dispose() {
		screenManager.dispose();
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
