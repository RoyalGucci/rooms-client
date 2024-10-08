package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.Repository;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.ScrollListener;
import net.rooms.client.ui.dashboard.DashboardScreen;

import java.util.Collection;
import java.util.HashMap;

class RoomsPanelList extends Table {

	private final DashboardScreen screen;
	private final Skin skin;
	private final Table scrollTable;
	private final HashMap<Long, TextButton> roomRows;

	public RoomsPanelList(DashboardScreen screen) {
		this.screen = screen;
		this.roomRows = new HashMap<>();
		skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		setBackground(skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1));

		scrollTable = new Table();
		scrollTable.top().left();
		ScrollPane scrollPane = new ScrollPane(scrollTable, skin);
		scrollPane.setFadeScrollBars(false);
		add(scrollPane).expand().fill();
		addListener(new ScrollListener(scrollTable));
	}

	public void putRoom(Room room) {
		TextButton roomRow = roomRows.get(room.roomID());
		if (roomRow == null) {
			roomRow = new TextButton(room.title(), skin);
			scrollTable.add(roomRow).pad(10).fillX().expandX().left().row();
		} else {
			roomRow.setText(room.title());
            roomRow.clearListeners();
        }

		roomRow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.currentRoomID = room.roomID();
				screen.getChat().resetContent();
				screen.getChat().setRoom(screen.currentRoomID);
				screen.getChat().setInteractive(true);
			}
		});
		roomRows.put(room.roomID(), roomRow);
	}

	public void removeRoom(long roomID) {
		roomRows.remove(roomID);
		scrollTable.clear();
		roomRows.values().forEach(roomRow -> scrollTable.add(roomRow).pad(10).fillX().expandX().left().row());
	}

	public void updateContent(Collection<Repository.RoomEntry> rooms) {
		resetContent();
		for (Repository.RoomEntry room : rooms) putRoom(room.room());
	}

	public void resetContent() {
		scrollTable.clear();
		roomRows.clear();
	}
}
