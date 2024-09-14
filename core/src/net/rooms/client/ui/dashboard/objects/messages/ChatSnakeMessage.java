package net.rooms.client.ui.dashboard.objects.messages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.*;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class ChatSnakeMessage extends ChatGameMessage {

	private final static FileHandle SNAKES = Gdx.files.internal("snakes.png");

	public ChatSnakeMessage(DashboardScreen screen, Message message, Skin skin) {
		super(screen, message, skin);
		SnakesConfig config = JSON.fromJson(message.content(), SnakesConfig.class);
		if (update.participants() == null) ParticipantCountReport.setText("1 / " + config.maxPlayers());
		else ParticipantCountReport.setText(update.participants().size() + " / " + config.maxPlayers());
	}

	@Override
	protected void initUI(Message message) {
		switch (message.type()) {
			case SNAKES_GAME_OPEN -> initGameOpenUI(message);
			case SNAKES_GAME_ONGOING -> initGameOngoingUI();
			case SNAKES_GAME_ABORT -> initGameAbortUI();
			case SNAKES_GAME_RESULT -> {
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
		System.out.println(update);
		SnakesConfig config = (SnakesConfig) update.config();
		ParticipantCountReport.setText(update.participants().size() + " / " + config.maxPlayers());
		joined = (update.participants() != null && update.participants().contains(username)) || message.sender().equals(username);
		if (joined) participation.setText("Leave");
		else participation.setText("Join");
	}

	@Override
	protected FileHandle icon() {
		return SNAKES;
	}
}