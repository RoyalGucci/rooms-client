package net.rooms.client.games.pong;

import com.badlogic.gdx.Gdx;
import net.rooms.client.Client;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.GameUpdate;
import net.rooms.client.games.pong.objects.GetPong;
import net.rooms.client.games.pong.objects.Player;
import net.rooms.client.games.pong.objects.SendPong;

public class PongGuestScreen extends PongScreen {

	private volatile SendPong lastUpdate;

	public PongGuestScreen(Client client, GameUpdate update, long id, String participant, String host) {
		super(client, update, id, participant, host);
		channelListener.setGuestChannelListener((sendPong -> Gdx.app.postRunnable(() -> PongGuestScreen.this.onNetworkEvent(sendPong))), SendPong.class);
	}

	public void onNetworkEvent(SendPong sendPong) {
		lastUpdate = sendPong;
	}

	@Override
	public void update() {
		super.update();
		if (lastUpdate != null) {
			ball.update(false, lastUpdate.ballX(), lastUpdate.ballY());
			lastUpdate.entries().forEach(entry -> getPlayer(entry.username()).update(entry.location()));
		}
		Player player = getPlayer(participant);
		player.update();
		GetPong getPong = new GetPong(player.getLocation(), participant);
		client.getApiRequests().sendToGameHost(gameID, JSON.toJson(getPong));
	}
}