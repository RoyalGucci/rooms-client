package net.rooms.client.games.pong;

import com.badlogic.gdx.Gdx;
import net.rooms.client.Client;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.GameUpdate;
import net.rooms.client.connection.objects.PongResults;
import net.rooms.client.games.pong.objects.GetPong;
import net.rooms.client.games.pong.objects.SendPong;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PongHostScreen extends PongScreen {

	private final Queue<GetPong> moves;

	public PongHostScreen(Client client, GameUpdate update, long id, String participant, String host) {
		super(client, update, id, participant, host);
		moves = new LinkedList<>();
		world.setContactListener(new GameContactListener(this));
		channelListener.setHostChannelListener((getPong -> Gdx.app.postRunnable(() -> PongHostScreen.this.onNetworkEvent(getPong))), GetPong.class);
	}

	public void onNetworkEvent(GetPong getPong) {
		if (getPong == null) return;
		moves.add(getPong);
	}

	@Override
	public void update() {
		super.update();
		ball.update(true, 0, 0);
		getPlayer(participant).update();
		moves.forEach(getPong -> getPlayer(getPong.username()).update(getPong.location()));
		moves.clear();

		ArrayList<SendPong.SendPongEntry> entries = new ArrayList<>();
		getPlayers().forEach(player -> entries.add(new SendPong.SendPongEntry(player.getUsername(), player.getLocation())));
		SendPong sendPong = new SendPong(entries, ball.getX(), ball.getY());
		client.getApiRequests().sendToGameGuests(gameID, JSON.toJson(sendPong));
	}

	@Override
	public void winner(String username) {
		super.winner(username);
		PongResults pongResults = new PongResults(username);
		client.getApiRequests().submitGame(gameID, JSON.toJson(pongResults));
	}
}