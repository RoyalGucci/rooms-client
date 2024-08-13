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

import java.util.List;

class RoomsPanelList extends Table {

	private final DashboardScreen screen;
	private final Skin skin;
	private final Table scrollTable;

	public RoomsPanelList(DashboardScreen screen) {
		this.screen = screen;
		skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		scrollTable = new Table();
		scrollTable.top().left();
		setBackground(skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1));

		ScrollPane scrollPane = new ScrollPane(scrollTable, skin);
		scrollPane.setFadeScrollBars(false);
		add(scrollPane).expand().fill();
	}

	public void addRoom(Room room) {
		TextButton roomRow = new TextButton(room.title(), skin);
		roomRow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.getChat().setRoom(room);
			}
		});
		scrollTable.add(roomRow).pad(10).fillX().expandX().left();
		scrollTable.row();
	}

	public void downloadContent() {
		List<Room> roomsList = screen.getClient().getApiRequests().getRooms();
		for (Room room : roomsList) addRoom(room);
	}

	public void resetContent() {
		scrollTable.clear();
	}
}
