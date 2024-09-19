package net.rooms.client.ui.search;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;
import net.rooms.client.connection.objects.PublicRoom;
import net.rooms.client.ui.search.objects.SearchChatInfo;
import net.rooms.client.ui.search.objects.SearchNavPanel;
import net.rooms.client.ui.search.objects.SearchRoomsPanel;

import java.util.HashMap;
import java.util.List;

public class SearchScreen implements Screen {

	private final Stage stage;
	private final Client client;
	private final SearchRoomsPanel roomsPanel;
	public long currentRoomID;
	private final SearchChatInfo chat;
	private final HashMap<Long,PublicRoom> publicRooms;

	public SearchScreen(Client client) {
		this.client = client;
		stage = new Stage(new ScreenViewport());
		this.publicRooms = new HashMap<>();

		Table root = new Table();
		root.setFillParent(true);
		chat = new SearchChatInfo(this);
		roomsPanel = new SearchRoomsPanel(this);
		root.add(new SearchNavPanel(this)).width(70).fillY().expandY();
		root.add(roomsPanel).width(300).fill();
		root.add(chat).expand().fill();
		stage.addActor(root);
	}

	public void setSearchedRooms(List<PublicRoom> updatedRooms) {
		for (PublicRoom room : updatedRooms) {
			publicRooms.put(room.roomID(),room);
		}
		roomsPanel.updateContent(updatedRooms);
	}

	public PublicRoom getRoom() {
		return publicRooms.get(currentRoomID);
	}

	public Stage getStage() {
		return stage;
	}

	public Client getClient() {
		return client;
	}

	public SearchChatInfo getChat() {
		return chat;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		chat.setInteractive(false);
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
		chat.setInteractive(false);
		chat.resetContent();
		currentRoomID = 0;
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
