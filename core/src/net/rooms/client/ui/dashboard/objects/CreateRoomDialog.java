package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.RoomsWindow;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class CreateRoomDialog extends RoomsWindow {
	public CreateRoomDialog(DashboardScreen screen, Skin skin) {
		super("Create New Room", skin);
		setSize(400, 400);
		setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
		TextField titleField = new TextField("", skin);
		titleField.setMessageText("Title");
		TextField descriptionField = new TextField("", skin);
		descriptionField.setMessageText("Description");
		TextField passwordField = new TextField("", skin);
		passwordField.setMessageText("password");
		TextButton create = new TextButton("create", skin);
		CheckBox disableCheckBox = new CheckBox("", skin);

		add(titleField).pad(10);
		row();
		add(descriptionField).pad(10).size(350, 28);
		row();
		add(passwordField).pad(10);
		row();
		add(new Label("private", skin));
		row();
		add(disableCheckBox);
		row();
		add(create).pad(10);

		create.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Room room = screen.getClient().getApiRequests().createRoom(titleField.getText(), descriptionField.getText(), disableCheckBox.isChecked(), passwordField.getText());
				screen.putRoom(room);
				remove();
			}
		});
	}
}
