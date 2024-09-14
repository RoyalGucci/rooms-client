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
import net.rooms.client.games.GameScreen;
import net.rooms.client.games.pong.PongGuestScreen;
import net.rooms.client.games.pong.PongHostScreen;
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

	private GameScreen gameScreen;

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
		client.getRepository().listEntries().forEach(entry -> roomsPanel.putRoom(entry.room()));
	}

	private void massagesListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
		chat.addMessage(message);
	}

	private void roomDetailsListener(Room room) {
		Repository.RoomEntry entry = new Repository.RoomEntry(room, client.getRepository().getEntry(room.roomID()).participants(), client.getRepository().getEntry(room.roomID()).messages());
		client.getRepository().putEntry(entry);
		chat.updateTitle(room.title());
		roomsPanel.putRoom(room);
	}

	private void joinListener(Participant participant) {
		if (participant.username().equals(client.getApiRequests().getUsername())) return;

		client.getRepository().getEntry(participant.roomID()).participants().put(participant.username(), participant);
	}

	private void leaveListener(Participant participant) {
		client.getRepository().getEntry(participant.roomID()).participants().remove(participant.username());
	}

	private void joinGameListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
		chat.addMessage(message);
	}

	private void leaveGameListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
		chat.addMessage(message);
		GameUpdate update = JSON.fromJson(message.content(), GameUpdate.class);
		if (update.username() == null || gameScreen == null) return;

		if (message.id() == gameScreen.gameID) {
			gameScreen.onDisconnect(update.username());
			if (client.getApiRequests().getUsername().equals(update.username())) gameScreen = null;
		}
	}

	private void startGameListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
		chat.addMessage(message);
		GameUpdate update = JSON.fromJson(message.content(), GameUpdate.class);
		if (!update.participants().contains(client.getApiRequests().getUsername())) return;

		if (message.sender().equals(client.getApiRequests().getUsername()))
			gameScreen = new PongHostScreen(client, update, message.id(), client.getApiRequests().getUsername(), message.sender());
		else gameScreen = new PongGuestScreen(client, update, message.id(), client.getApiRequests().getUsername(), message.sender());
		client.setScreen(gameScreen);
	}

	private void gameResultsListener(Message message) {
		client.getRepository().getEntry(message.roomID()).messages().put(message.id(), message);
		chat.addMessage(message);
		if (gameScreen == null) return;

		if (message.id() == gameScreen.gameID) {
			gameScreen.onDisconnect(client.getApiRequests().getUsername());
			gameScreen = null;
		}
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
		client.getApiRequests().setWSListener("messages", (message -> Gdx.app.postRunnable(() -> DashboardScreen.this.massagesListener(message))), Message.class);
		client.getApiRequests().setWSListener("description", (room -> Gdx.app.postRunnable(() -> DashboardScreen.this.roomDetailsListener(room))), Room.class);
		client.getApiRequests().setWSListener("title", (room -> Gdx.app.postRunnable(() -> DashboardScreen.this.roomDetailsListener(room))), Room.class);
		client.getApiRequests().setWSListener("join", (participant -> Gdx.app.postRunnable(() -> DashboardScreen.this.joinListener(participant))), Participant.class);
		client.getApiRequests().setWSListener("leave", (participant -> Gdx.app.postRunnable(() -> DashboardScreen.this.leaveListener(participant))), Participant.class);
		client.getApiRequests().setWSListener("game/join", (message -> Gdx.app.postRunnable(() -> DashboardScreen.this.joinGameListener(message))), Message.class);
		client.getApiRequests().setWSListener("game/leave", (message -> Gdx.app.postRunnable(() -> DashboardScreen.this.leaveGameListener(message))), Message.class);
		client.getApiRequests().setWSListener("game/start", (message -> Gdx.app.postRunnable(() -> DashboardScreen.this.startGameListener(message))), Message.class);
		client.getApiRequests().setWSListener("game/results", (message -> Gdx.app.postRunnable(() -> DashboardScreen.this.gameResultsListener(message))), Message.class);

		client.getApiRequests().getRooms().forEach(this::putRoom);
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
