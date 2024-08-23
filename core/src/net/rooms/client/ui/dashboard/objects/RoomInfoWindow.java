package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class RoomInfoWindow extends Window {
	//TODO: ADD INFORMATION DISPLAY, DESCRIPTION AND PARTICIPANTS
	public RoomInfoWindow(DashboardScreen screen, Skin skin) {
		super("Change Room Details", skin);
		setSize(500, 300);
		setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
		TextField titleField = new TextField(screen.getRoom(screen.currentRoomID).room().title(), skin);
		titleField.setMessageText("Title");
		TextField descriptionField = new TextField(screen.getRoom(screen.currentRoomID).room().description(), skin);
		descriptionField.setMessageText("Description");
		TextButton changeTitle = new TextButton("change", skin);
		TextButton changeDescription = new TextButton("change", skin);
		TextButton finish = new TextButton("close", skin);
		TextButton exit = new TextButton("exit room", skin);
		Label errorMessage = new Label("", skin);
		errorMessage.setColor(1, 0, 0, 1);

		add(titleField).pad(10);
		add(changeTitle).pad(10);
		row();
		add(descriptionField).pad(10).size(300, 28);
		add(changeDescription).pad(10);
		row();
		add(finish).pad(10);
		row();
		add(errorMessage).pad(10);
		row();
		add(exit).pad(10);

		changeTitle.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!screen.getClient().getApiRequests().updateTitle(screen.currentRoomID, titleField.getText()))
					errorMessage.setText("could not change title");
			}
		});

		changeDescription.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!screen.getClient().getApiRequests().updateDescription(screen.currentRoomID, descriptionField.getText()))
					errorMessage.setText("could not change description");
			}
		});

		finish.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				remove();
			}
		});

		exit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!screen.getClient().getApiRequests().leaveRoom(screen.currentRoomID)) {
					errorMessage.setText("could not leave room");
					return;
				}
				screen.removeRoom(screen.currentRoomID);
				remove();
			}
		});
	}
}
