package net.rooms.client.ui.dashboard.objects.messages;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.GameUpdate;
import net.rooms.client.connection.objects.Message;
import net.rooms.client.ui.dashboard.DashboardScreen;

public abstract class ChatGameMessage extends Table {

	protected final Label ParticipantCountReport;
	protected final Label hostLabel;
	protected final ImageButton gameIcon;
	protected final TextButton start;
	protected final TextButton participation;
	protected final Label textLabel;
	protected final String username;

	protected GameUpdate update;
	protected Message message;

	protected boolean joined;

	public ChatGameMessage(DashboardScreen screen, Message message, Skin skin) {
		super(skin);
		update = JSON.fromJson(message.content(), GameUpdate.class);
		this.message = message;
		ParticipantCountReport = new Label("", skin);
		username = screen.getClient().getApiRequests().getUsername();
		joined = (update.participants() != null && update.participants().contains(username)) || message.sender().equals(username);
		textLabel = new Label("", skin);

		hostLabel = new Label(" " + message.sender(), skin);
		gameIcon = new ImageButton(new TextureRegionDrawable(new Texture(icon())));
		start = new TextButton("Start", skin);
		participation = new TextButton(joined ? "Leave" : "Join", skin);
		setBackground(skin.newDrawable("white", 0.7f, 0.7f, 0.7f, 1));

		start.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (update.participants() == null || update.participants().size() <= 1) return;
				screen.getClient().getApiRequests().startGame(message.id());
			}
		});

		participation.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (joined) screen.getClient().getApiRequests().leaveGame(message.id());
				else screen.getClient().getApiRequests().joinGame(message.id());
			}
		});

		initUI(message);
	}

	protected void initGameOpenUI(Message message) {
		add(hostLabel).expandX().left().fillX().row();
		add(gameIcon).expandX().left().fillX();
		add(ParticipantCountReport).expandX().left().fillX();
		if (message.sender().equals(username)) add(start).expandX().right().fillX().padRight(10);
		add(participation).expandX().right().fillX();
	}

	protected void initGameOngoingUI() {
		add(gameIcon).expandX().left().fillX();
		textLabel.setText("Ongoing...");
		add(textLabel).expandX().right().fillX();
	}

	protected void initGameAbortUI() {
		add(gameIcon).expandX().left().fillX();
		textLabel.setText("Aborted");
		add(textLabel).expandX().right().fillX();
	}

	protected abstract void initUI(Message message);

	public abstract void update(Message message);

	protected abstract FileHandle icon();
}
