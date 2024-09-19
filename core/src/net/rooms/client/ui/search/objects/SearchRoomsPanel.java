package net.rooms.client.ui.search.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.rooms.client.connection.objects.PublicRoom;
import net.rooms.client.ui.search.SearchScreen;

import java.util.Collection;

public class SearchRoomsPanel extends Table {

	private final SearchRoomsPanelHeader roomsPanelHeader;
	private final SearchRoomsPanelList roomsPanelList;

	public SearchRoomsPanel(SearchScreen screen) {
		roomsPanelList = new SearchRoomsPanelList(screen);
		roomsPanelHeader = new SearchRoomsPanelHeader(screen);
		top().left();
		add(roomsPanelHeader).expandX().fillX();
		row();
		add(roomsPanelList).expand().fill();
	}

	public void updateContent(Collection<PublicRoom> rooms) {
		roomsPanelList.updateContent(rooms);
	}

	public void resetContent() {
		roomsPanelList.resetContent();
		roomsPanelHeader.resetContent();
	}
}

