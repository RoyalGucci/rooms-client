package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.ui.RoomsWindow;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class AddPlayerWindow extends RoomsWindow {

	public AddPlayerWindow(DashboardScreen screen, Skin skin) {
		super("Add a player", skin);
		setSize(500, 150);
		setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
		TextField usernameField = new TextField("", skin);
		usernameField.setMessageText("username");
		TextButton add = new TextButton("Add", skin);
		Label errorMessage = new Label("", skin);
		errorMessage.setColor(1, 0, 0, 1);

		row();
		add(usernameField).pad(10).size(300, 28);
		add(add).pad(10);
		row();
		add(errorMessage).pad(10);
		row();

		add.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!screen.getClient().getApiRequests().inviteToRoom(screen.currentRoomID, usernameField.getText()))
					errorMessage.setText("Could not invite player to room");
				else{
					errorMessage.setText("Player added successfully");
				}
			}
		});
	}
}
