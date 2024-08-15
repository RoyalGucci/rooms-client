package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class RoomInfoWindow extends Window {
	//TODO: ADD INFORMATION DISPLAY, DESCRIPTION AND PARTICIPANTS
	public RoomInfoWindow(DashboardScreen screen, Skin skin) {
		super("Change Room Details", skin);
		setSize(500, 200);
		setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
		TextField titleField = new TextField("", skin);
		titleField.setMessageText("Title");
		TextField descriptionField = new TextField("", skin);
		descriptionField.setMessageText("Description");
		TextButton changeTitle = new TextButton("change", skin);
		TextButton changeDescription = new TextButton("change", skin);
		TextButton finish = new TextButton("finish", skin);

		add(titleField).pad(10);
		add(changeTitle).pad(10);
		row();
		add(descriptionField).pad(10).size(300, 28);
		add(changeDescription).pad(10);
		row();
		add(finish).pad(10);

		changeTitle.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (screen.getClient().getApiRequests().updateTitle(screen.currentRoomID, titleField.getText())) {
					Room room = screen.getRoom(screen.currentRoomID);
					Room updatedRoom = new Room(room.roomID(), titleField.getText(), room.isPrivate(), room.password(), room.owner(), room.creationDate(), room.description());
					screen.putRoom(updatedRoom);
				}
				//TODO: ADD GUI
				else{
					System.out.println("could not change title");
				}
			}
		});

		changeDescription.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (screen.getClient().getApiRequests().updateDescription(screen.currentRoomID, descriptionField.getText())) {
					Room room = screen.getRoom(screen.currentRoomID);
					Room updatedRoom = new Room(room.roomID(), room.title(), room.isPrivate(), room.password(), room.owner(), room.creationDate(), descriptionField.getText());
					screen.putRoom(updatedRoom);
				}
				//TODO: ADD GUI
				else{
					System.out.println("could not change description");
				}
			}
		});

		finish.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				remove();
			}
		});
	}
}
