package net.rooms.client.ui.dashboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;
import net.rooms.client.JSON;
import net.rooms.client.Repository;
import net.rooms.client.connection.objects.GameUpdate;
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

	public Stage getStage() {
		return stage;
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

	public void removeRoom(long roomID) {
		if (roomID == currentRoomID) {
			chat.setInteractive(false);
			chat.resetContent();
		}
		roomsPanel.removeRoom(roomID);
		client.getRepository().removeEntry(roomID);
	}

	public void putRoom(Room room) {
		client.getRepository().putEntry(new Repository.RoomEntry(room, client.getApiRequests().getParticipants(room.roomID()), client.getApiRequests().getMessages(room.roomID())));
		roomsPanel.putRoom(room);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		client.getApiRequests().getRooms().forEach(this::putRoom);
	}

	private void massagesListener(Message message) {
		if (currentRoomID == message.roomID())
			chat.addMessage(message.content(), message.sender(), client.getApiRequests().getUsername().equals(message.sender()));
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
	}

	private void roomDetailsListener(Room room) {
		Repository.RoomEntry entry = new Repository.RoomEntry(room, client.getRepository().getEntry(room.roomID()).participants(), client.getRepository().getEntry(room.roomID()).messages());
		client.getRepository().putEntry(entry);
		chat.updateTitle(room.title());
		roomsPanel.putRoom(room);
	}

	private void joinListener(Participant participant) {
		client.getRepository().getEntry(participant.roomID()).participants().put(participant.username(), participant);
	}

	private void leaveListener(Participant participant) {
		client.getRepository().getEntry(participant.roomID()).participants().remove(participant.username());
	}

	private void joinGameListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
		GameUpdate update = JSON.fromJson(message.content(), GameUpdate.class);
		// TODO update related message UI using update and message::type. Use message::type or instanceof to tell update::config actual type
	}

	private void leaveGameListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
		GameUpdate update = JSON.fromJson(message.content(), GameUpdate.class);
		// TODO update related message UI using update and message::type. Use message::type or instanceof to tell update::config actual type
	}

	private void startGameListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
		GameUpdate update = JSON.fromJson(message.content(), GameUpdate.class);
		// TODO update related message UI using update and message::type. Use message::type or instanceof to tell update::config actual type
		// TODO start the game for participants. See update::participants
	}

	private void gameResultsListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
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

	public void loadDashboard() {
		client.getApiRequests().setWSListener("messages", this::massagesListener, Message.class);
		client.getApiRequests().setWSListener("description", this::roomDetailsListener, Room.class);
		client.getApiRequests().setWSListener("title", this::roomDetailsListener, Room.class);
		client.getApiRequests().setWSListener("join", this::joinListener, Participant.class);
		client.getApiRequests().setWSListener("leave", this::leaveListener, Participant.class);
		client.getApiRequests().setWSListener("game/join", this::joinGameListener, Message.class);
		client.getApiRequests().setWSListener("game/leave", this::leaveGameListener, Message.class);
		client.getApiRequests().setWSListener("game/start", this::startGameListener, Message.class);
		client.getApiRequests().setWSListener("game/results", this::gameResultsListener, Message.class);

		chat.setInteractive(false);
	}

	public void unloadDashboard() {
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
