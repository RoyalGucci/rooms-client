package net.rooms.client.ui.signup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;
import net.rooms.client.ui.signup.objects.Signup;

public class SignupScreen implements Screen {

	private final Stage stage;
	private final Client client;
	private final Signup signup;

	public SignupScreen(final Client client) {
		this.client = client;
		stage = new Stage(new ScreenViewport());
		signup = new Signup(this);
		stage.addActor(signup);
	}

	public Client getClient() {
		return client;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		signup.onShow();
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
