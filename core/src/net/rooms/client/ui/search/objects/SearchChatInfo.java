package net.rooms.client.ui.search.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.Repository;
import net.rooms.client.connection.objects.PublicRoom;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.search.SearchScreen;

import java.util.Optional;

public class SearchChatInfo extends Table {

	private final Skin skin;
	private final Table chatContainer;
	private final SearchScreen screen;

	public SearchChatInfo(SearchScreen screen) {
		this.screen = screen;
		skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		setBackground(skin.newDrawable("white", 0.8f, 0.8f, 0.8f, 1));

		chatContainer = new Table();
		chatContainer.top();
		ScrollPane scrollPane = new ScrollPane(chatContainer, skin);
		scrollPane.setFadeScrollBars(false);
		add(scrollPane).expand().fill().pad(10);
		row();
	}

	public void setInteractive(boolean interactive) {
		Touchable touchable = interactive ? Touchable.enabled : Touchable.disabled;
		chatContainer.setTouchable(touchable);
	}

	public void setRoom() {
		PublicRoom current = screen.getRoom();
		TextButton joinButton = new TextButton("Join", skin);
		TextField password = new TextField("", skin);
		Label info = new Label("", skin);
		info.setColor(Color.RED);
		password.setMessageText("password");
		chatContainer.add(new Label(current.title(), skin)).pad(20);
		chatContainer.row();
		chatContainer.add(new Label(current.description(), skin)).pad(20);
		chatContainer.row();
		if (current.hasPassword()) {
			chatContainer.add(password).pad(20);
			chatContainer.row();
		}
		chatContainer.add(joinButton).pad(20);
		chatContainer.row();
		chatContainer.add(info).pad(20);

		joinButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (screen.getClient().getApiRequests().joinRoom(current.roomID(), password.getText())) {
					Optional<Room> optional = screen.getClient().getApiRequests().getRooms().stream().filter(roomEntry -> roomEntry.roomID() == current.roomID()).findFirst();
					if (optional.isEmpty()) return;
					Room room = optional.get();
					screen.getClient().getRepository().putEntry(new Repository.RoomEntry(room, screen.getClient().getApiRequests().getParticipants(current.roomID()), screen.getClient().getApiRequests().getMessages(room.roomID())));
					info.setText("Joined successfully");
				} else {
					info.setText("Error, Could not join");
				}

			}
		});
	}

	public void resetContent() {
		chatContainer.clearChildren();
		screen.getStage().setKeyboardFocus(chatContainer);
	}
}
