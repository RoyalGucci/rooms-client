package net.rooms.client.ui.dashboard.objects.messages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.GameUpdate;
import net.rooms.client.connection.objects.Message;
import net.rooms.client.connection.objects.PongConfig;
import net.rooms.client.connection.objects.PongResults;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class ChatPongMessage extends ChatGameMessage {

	private final static FileHandle PONG = Gdx.files.internal("Pong.png");

	public ChatPongMessage(DashboardScreen screen, Message message, Skin skin) {
		super(screen, message, skin);
		PongConfig config = JSON.fromJson(message.content(), PongConfig.class);
		if (update.participants() == null) ParticipantCountReport.setText("1 / " + config.maxPlayers());
		else ParticipantCountReport.setText(update.participants().size() + " / " + config.maxPlayers());
	}

	@Override
	protected void initUI(Message message) {
		switch (message.type()) {
			case PONG_GAME_OPEN -> initGameOpenUI(message);
			case PONG_GAME_ONGOING -> initGameOngoingUI();
			case PONG_GAME_ABORT -> initGameAbortUI();
			case PONG_GAME_RESULT -> {
				PongResults pongResults = JSON.fromJson(message.content(), PongResults.class);
				add(gameIcon).expandX().left().fillX();
				textLabel.setText(pongResults.toString());
				add(textLabel).expandX().right().fillX();
			}
		}
	}

	@Override
	public void update(Message message) {
		if (this.message.type() != message.type()) {
			clear();
			this.message = message;
			initUI(message);
			return;
		}
		update = JSON.fromJson(message.content(), GameUpdate.class);
		PongConfig config = (PongConfig) update.config();
		ParticipantCountReport.setText(update.participants().size() + " / " + config.maxPlayers());
		joined = (update.participants() != null && update.participants().contains(username)) || message.sender().equals(username);
		if (joined) participation.setText("Leave");
		else participation.setText("Join");
	}

	@Override
	protected FileHandle icon() {
		return PONG;
	}
}