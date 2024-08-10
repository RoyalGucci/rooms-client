package net.rooms.client.ui.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;
import net.rooms.client.ui.login.objects.Login;

public class LoginScreen implements Screen {

	private final Stage stage;
	private final Client client;
	private final Login login;

	public LoginScreen(final Client client) {
		this.client = client;
		stage = new Stage(new ScreenViewport());
		login = new Login(this);
		stage.addActor(login);
	}

	public Client getClient() {
		return client;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		login.onShow();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
