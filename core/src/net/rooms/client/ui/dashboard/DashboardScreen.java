package net.rooms.client.ui.dashboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.dashboard.objects.Chat;
import net.rooms.client.ui.dashboard.objects.NavPanel;
import net.rooms.client.ui.dashboard.objects.RoomsPanel;

import java.util.HashMap;
import java.util.List;

public class DashboardScreen implements Screen {

	private final Stage stage;
	private final Client client;
	private final RoomsPanel roomsPanel;
	public long currentRoomID;
	private final HashMap<Long, Room> rooms;

	private final Chat chat;

	public DashboardScreen(Client client) {
		this.client = client;
		stage = new Stage(new ScreenViewport());
		rooms = new HashMap<>();

		Table root = new Table();
		root.setFillParent(true);
		chat = new Chat(this);
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

	public Room getRoom(long roomID) {
		return rooms.get(roomID);
	}

	public void putRoom(Room room) {
		rooms.put(room.roomID(), room);
		roomsPanel.putRoom(room);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		List<Room> rooms = client.getApiRequests().getRooms();
		rooms.forEach(room -> this.rooms.put(room.roomID(), room));
		roomsPanel.updateContent(rooms);
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
		currentRoomID = 0;
		rooms.clear();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
