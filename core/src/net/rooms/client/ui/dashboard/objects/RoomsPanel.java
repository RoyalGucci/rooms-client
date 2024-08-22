package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.rooms.client.Repository;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.ui.dashboard.DashboardScreen;

import java.util.List;

public class RoomsPanel extends Table {

	private final RoomsPanelHeader roomsPanelHeader;
	private final RoomsPanelList roomsPanelList;

	public RoomsPanel(DashboardScreen screen) {
		roomsPanelList = new RoomsPanelList(screen);
		roomsPanelHeader = new RoomsPanelHeader(screen);
		top().left();
		add(roomsPanelHeader).expandX().fillX();
		row();
		add(roomsPanelList).expand().fill();
	}

	public void putRoom(Room room) {
		roomsPanelList.putRoom(room);
	}

	public void updateContent(List<Repository.RoomEntry> rooms) {
		roomsPanelList.updateContent(rooms);
	}

	public void resetContent() {
		roomsPanelList.resetContent();
		roomsPanelHeader.resetContent();
	}
}
