package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class Chat extends Table {

	private final Skin skin;
	private final Table chatContainer;
	private final TextField inputField;
	private final Label title;
	private final DashboardScreen screen;

	public Chat(DashboardScreen screen) {
        this.screen = screen;
        skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
        setBackground(skin.newDrawable("white", 0.8f, 0.8f, 0.8f, 1));
		// Upper panel (the title section)
		Table upperPanel = new Table();
		title = new Label("", skin);
		ImageButton gameMenu = new ImageButton(skin);
		upperPanel.add(title).expandX().left().pad(10);
		upperPanel.add(gameMenu).right().pad(10);
		add(upperPanel).expandX().fillX().top();
		row();

		// Center panel (chat area with scroll pane)
		chatContainer = new Table();
		chatContainer.top();
		ScrollPane scrollPane = new ScrollPane(chatContainer, skin);
		scrollPane.setFadeScrollBars(false);
		add(scrollPane).expand().fill().pad(10);
		row();

		// Bottom panel (input field)
		Table bottomPanel = new Table();
		inputField = new TextArea("", skin);
		inputField.setMessageText("Type a message...");
		inputField.setAlignment(Align.left);
		inputField.addListener(new ChatBoxListener());
		bottomPanel.add(inputField).expand().fill().pad(10);
		add(bottomPanel).height(60).expandX().fillX().bottom();

		title.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getStage().addActor(new RoomInfoWindow(screen, skin));
			}
		});

		gameMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//TODO: CREATE A GAME MANU WINDOW
			}
		});
	}

	// TODO: implement methods to add messages by types
	public void addMessage(String message) {
		chatContainer.row();
		chatContainer.add(new ChatMessage(message, skin)).expandX().fillX().pad(5);
	}

	public void setRoom(Room room) {
		title.setText(room.title());
		// TODO: download messages for this room from the server
	}

	public void resetContent() {
		chatContainer.clearChildren();
		title.setText("");
		inputField.setText("");
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
				// TODO: send message to server
				String message = inputField.getText(); // maybe create a json
				addMessage(message);
				inputField.setText("");
			}
			return true;
		}
	}
}

