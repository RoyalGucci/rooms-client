package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import net.rooms.client.Repository;
import net.rooms.client.connection.objects.Message;
import net.rooms.client.ui.ScrollListener;
import net.rooms.client.ui.dashboard.DashboardScreen;
import net.rooms.client.ui.dashboard.objects.messages.ChatGameMessage;
import net.rooms.client.ui.dashboard.objects.messages.ChatPongMessage;
import net.rooms.client.ui.dashboard.objects.messages.ChatSnakeMessage;

import java.util.HashMap;

import static net.rooms.client.connection.objects.MessageType.MESSAGE;

public class Chat extends Table {

	private final Skin skin;
	private final Table chatContainer;
	private final ScrollPane scrollPane;
	private final Table upperPanel;
	private final Table bottomPanel;
	private final TextField inputField;
	private final Label title;
	private final DashboardScreen screen;

	private final HashMap<Long, ChatGameMessage> gameMessages;


	public Chat(DashboardScreen screen) {
		this.screen = screen;
		skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		gameMessages = new HashMap<>();
		setBackground(skin.newDrawable("white", 0.8f, 0.8f, 0.8f, 1));
		// Upper panel (the title section)
		upperPanel = new Table();
		title = new Label("", skin);
		Texture texture = new Texture(Gdx.files.internal("game.png"));
		ImageButton gameMenu = new ImageButton(new TextureRegionDrawable(texture));
		Texture texture2 = new Texture(Gdx.files.internal("players.png"));
		ImageButton participants = new ImageButton(new TextureRegionDrawable(texture2));
		Texture texture3 = new Texture(Gdx.files.internal("settings.png"));
		ImageButton settings = new ImageButton(new TextureRegionDrawable(texture3));
		upperPanel.setBackground(skin.newDrawable("white", 0.9f, 0.9f, 0.9f, 1));
		upperPanel.add(title).expandX().left().pad(10);
		upperPanel.add(settings).right().pad(10);
		upperPanel.add(participants).right().pad(10);
		upperPanel.add(gameMenu).right().pad(10);
		add(upperPanel).expandX().fillX().top();
		row();
		add().height(10);
		row();

		// Center panel (chat area with scroll pane)
		chatContainer = new Table();
		chatContainer.top();
		scrollPane = new ScrollPane(chatContainer, skin);
		scrollPane.setFadeScrollBars(false);
		add(scrollPane).expand().fill().pad(10);
		row();

		// Bottom panel (input field)
		bottomPanel = new Table();
		inputField = new TextArea("", skin);
		inputField.setMessageText("Type a message...");
		inputField.setAlignment(Align.left);
		inputField.addListener(new ChatBoxListener());
		bottomPanel.add(inputField).expand().fill().pad(10);
		add(bottomPanel).height(60).expandX().fillX().bottom();

		chatContainer.addListener(new ScrollListener(chatContainer));

		settings.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getStage().addActor(new RoomInfoWindow(screen, skin));
			}
		});

		gameMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getStage().addActor(new CreateGameWindow(screen, skin));
			}
		});

		participants.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getStage().addActor(new PlayersWindow(screen, skin));
			}
		});
	}

	public void setInteractive(boolean interactive) {
		Touchable touchable = interactive ? Touchable.enabled : Touchable.disabled;
		upperPanel.setTouchable(touchable);
		chatContainer.setTouchable(touchable);
		bottomPanel.setTouchable(touchable);
	}

	// TODO: implement methods to add messages by types
	public void addMessage(Message message) {
		if (screen.currentRoomID != message.roomID()) return;
		if (gameMessages.containsKey(message.id())) {
			gameMessages.get(message.id()).update(message);
			switch (message.type()) {
				case PONG_GAME_ABORT, SNAKES_GAME_ABORT -> gameMessages.remove(message.id());
			}
			return;
		}
		switch (message.type()) {
			case MESSAGE -> {
				chatContainer.row();
				chatContainer.add(new ChatMessage(message.content(), message.sender(), skin)).expandX().fillX().pad(5);
			}
			case PONG_GAME_OPEN, PONG_GAME_ONGOING,PONG_GAME_RESULT,PONG_GAME_ABORT -> {
				chatContainer.row();
				ChatPongMessage gameMessage = new ChatPongMessage(screen, message, skin);
				gameMessages.put(message.id(), gameMessage);
				chatContainer.add(gameMessage).expandX().fillX().pad(5);
			}
			case SNAKES_GAME_OPEN, SNAKES_GAME_ONGOING, SNAKES_GAME_RESULT,SNAKES_GAME_ABORT -> {
				chatContainer.row();
				ChatSnakeMessage gameMessage = new ChatSnakeMessage(screen, message, skin);
				gameMessages.put(message.id(), gameMessage);
				chatContainer.add(gameMessage).expandX().fillX().pad(5);
			}
		}
		scrollPane.scrollTo(0, 0, 0, scrollPane.getHeight(), false, true);
		scrollPane.updateVisualScroll();
	}

	public void setRoom(long roomID) {
		Repository.RoomEntry current = screen.getRoom(roomID);
		title.setText(current.room().title());
		current.getMessagesSortedByDate().forEach(this::addMessage);
	}

	public void updateTitle(String title) {
		this.title.setText(title);
	}

	public void resetContent() {
		gameMessages.clear();
		chatContainer.clearChildren();
		title.setText("");
		inputField.setText("");
		getStage().setKeyboardFocus(chatContainer);
	}

	private final class ChatBoxListener extends InputListener {
		boolean shift;

		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT)
				shift = true;
			return true;
		}

		@Override
		public boolean keyUp(InputEvent event, int keycode) {
			if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT)
				shift = false;
			return true;
		}

		@Override
		public boolean keyTyped(InputEvent event, char character) {
			if (!shift && character == '\n') {
				String message = inputField.getText();
				screen.getClient().getApiRequests().message(screen.getRoom(screen.currentRoomID).room().roomID(), MESSAGE, message);
				inputField.setText("");
			}
			return true;
		}
	}
}

