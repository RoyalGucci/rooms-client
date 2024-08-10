package net.rooms.client.ui.dashboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;
import net.rooms.client.connection.requests.Room;
import net.rooms.client.ui.dashboard.objects.Chat;
import net.rooms.client.ui.dashboard.objects.NavPanel;
import net.rooms.client.ui.dashboard.objects.RoomsPanel;

public class DashboardScreen implements Screen {

	private final Stage stage;
	private final Client client;
	private final RoomsPanel roomsPanel;

	private final Chat chat;

	public DashboardScreen(Client client) {
		this.client = client;
		stage = new Stage(new ScreenViewport());

		Table root = new Table();
		root.setFillParent(true);
		chat = new Chat();
		roomsPanel = new RoomsPanel(this);
		root.add(new NavPanel(this)).width(50).fillY().expandY();
		root.add(roomsPanel).width(300).fill();
		root.add(chat).expand().fill();
		stage.addActor(root);
	}

	public Client getClient() {
		return client;
	}

	public Chat getChat() {
		return chat;
	}

	public void addRoom(Room room) {
		roomsPanel.addRoom(room);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		roomsPanel.downloadContent();
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
		roomsPanel.resetContent();
		chat.resetContent();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
