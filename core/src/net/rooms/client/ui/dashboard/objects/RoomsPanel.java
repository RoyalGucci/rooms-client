package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.rooms.client.connection.requests.Room;
import net.rooms.client.ui.dashboard.DashboardScreen;

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

	public void addRoom(Room room) {
		roomsPanelList.addRoom(room);
	}

	public void downloadContent() {
		roomsPanelList.downloadContent();
	}

	public void resetContent() {
		roomsPanelList.resetContent();
		roomsPanelHeader.resetContent();
	}
}
