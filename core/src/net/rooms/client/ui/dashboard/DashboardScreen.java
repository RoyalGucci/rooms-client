package net.rooms.client.ui.dashboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;
import net.rooms.client.Repository;
import net.rooms.client.connection.objects.Message;
import net.rooms.client.connection.objects.Participant;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.dashboard.objects.Chat;
import net.rooms.client.ui.dashboard.objects.NavPanel;
import net.rooms.client.ui.dashboard.objects.RoomsPanel;

import java.util.ArrayList;
import java.util.List;

public class DashboardScreen implements Screen {

	private final Stage stage;
	private final Client client;
	private final RoomsPanel roomsPanel;
	public long currentRoomID;
	private final Chat chat;

	public DashboardScreen(Client client) {
		this.client = client;
		stage = new Stage(new ScreenViewport());

		Table root = new Table();
		root.setFillParent(true);
		chat = new Chat(this);
		roomsPanel = new RoomsPanel(this);
		root.add(new NavPanel(this)).width(70).fillY().expandY();
		root.add(roomsPanel).width(300).fill();
		root.add(chat).expand().fill();
		stage.addActor(root);
	}

	public List<Repository.RoomEntry> getRooms() {
		return new ArrayList<>(client.getRepository().listEntries());
	}

	public void setSearchedRooms(List<Repository.RoomEntry> updatedRooms) {
		roomsPanel.updateContent(updatedRooms);
	}

	public Client getClient() {
		return client;
	}

	public Chat getChat() {
		return chat;
	}

	public Repository.RoomEntry getRoom(long roomID) {
		return client.getRepository().getEntry(roomID);
	}

	public void deleteRoom(long roomID) {
		client.getRepository().removeEntry(roomID);
		List<Repository.RoomEntry> updatedRooms = new ArrayList<>(client.getRepository().listEntries());
		roomsPanel.updateContent(updatedRooms);
	}

	public void putRoom(Room room) {
		client.getRepository().putEntry(new Repository.RoomEntry(room, client.getApiRequests().getParticipants(room.roomID()), client.getApiRequests().getMessages(room.roomID())));
		roomsPanel.putRoom(room);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		List<Room> rooms = client.getApiRequests().getRooms();
		List<Repository.RoomEntry> roomEntries = new ArrayList<>();
		for (Room room : rooms) {
			Repository.RoomEntry entry = new Repository.RoomEntry(room, client.getApiRequests().getParticipants(room.roomID()), client.getApiRequests().getMessages(room.roomID()));
			roomEntries.add(entry);
			client.getRepository().putEntry(entry);
		}
		roomsPanel.updateContent(roomEntries);
		client.getApiRequests().setWSListener("messages", this::massagesListener, Message.class);
		client.getApiRequests().setWSListener("description", this::roomDetailsListener, Room.class);
		client.getApiRequests().setWSListener("title", this::roomDetailsListener, Room.class);
		client.getApiRequests().setWSListener("join", this::joinListener, Participant.class);
		client.getApiRequests().setWSListener("leave", this::leaveListener, Participant.class);
		chat.setInactive();
	}

	private void massagesListener(Message message) {
		if (currentRoomID == message.roomID())
			chat.addMessage(message.content(), message.sender(), client.getApiRequests().getUsername().equals(message.sender()));
		client.getRepository().getEntry(message.roomID()).messages().add(message);
	}

	private void roomDetailsListener(Room room) {
		Repository.RoomEntry entry = new Repository.RoomEntry(room, client.getRepository().getEntry(room.roomID()).participants(), client.getRepository().getEntry(room.roomID()).messages());
		client.getRepository().putEntry(entry);
	}

	private void joinListener(Participant participant) {
		client.getRepository().getEntry(participant.roomID()).participants().put(participant.username(), participant);
	}

	private void leaveListener(Participant participant) {
		client.getRepository().getEntry(participant.roomID()).participants().remove(participant.username());
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
		//rooms.clear();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
