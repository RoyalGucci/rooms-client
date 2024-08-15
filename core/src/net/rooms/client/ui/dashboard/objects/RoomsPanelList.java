package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.dashboard.DashboardScreen;

import java.util.HashMap;
import java.util.List;

class RoomsPanelList extends Table {

	private final DashboardScreen screen;
	private final Skin skin;
	private final Table scrollTable;
	private final HashMap<Long, TextButton> roomRows;

	public RoomsPanelList(DashboardScreen screen) {
		this.screen = screen;
		this.roomRows = new HashMap<>();
		skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		scrollTable = new Table();
		scrollTable.top().left();
		setBackground(skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1));

		ScrollPane scrollPane = new ScrollPane(scrollTable, skin);
		scrollPane.setFadeScrollBars(false);
		add(scrollPane).expand().fill();
	}

	public void putRoom(Room room) {
		TextButton roomRow = roomRows.get(room.roomID());
		if (roomRow == null) {
			roomRow = new TextButton(room.title(), skin);
			scrollTable.add(roomRow).pad(10).fillX().expandX().left();
			scrollTable.row();
		} else {
			roomRow.setText(room.title());
			screen.getChat().setRoom(room);
            roomRow.clearListeners();
        }

		roomRow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.getChat().setRoom(room);
				screen.currentRoomID = room.roomID();
			}
		});
		roomRows.put(room.roomID(), roomRow);
	}

	public void updateContent(List<Room> rooms) {
		for (Room room : rooms) putRoom(room);
	}

	public void resetContent() {
		scrollTable.clear();
		roomRows.clear();
	}
}
