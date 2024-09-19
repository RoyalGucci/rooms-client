package net.rooms.client.ui.search.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.connection.objects.PublicRoom;
import net.rooms.client.ui.ScrollListener;
import net.rooms.client.ui.search.SearchScreen;

import java.util.Collection;
import java.util.HashMap;

class SearchRoomsPanelList extends Table {

	private final SearchScreen screen;
	private final Skin skin;
	private final Table scrollTable;
	private final HashMap<Long, TextButton> roomRows;

	public SearchRoomsPanelList(SearchScreen screen) {
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

	public void putRoom(PublicRoom room) {
		TextButton roomRow = roomRows.get(room.roomID());
		if (roomRow == null) {
			roomRow = new TextButton(room.title(), skin);
			scrollTable.add(roomRow).pad(10).fillX().expandX().left();
			scrollTable.row();
		} else {
			roomRow.setText(room.title());
			roomRow.clearListeners();
		}

		roomRow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.currentRoomID = room.roomID();
				screen.getChat().resetContent();
				screen.getChat().setRoom();
				screen.getChat().setInteractive(true);
			}
		});
		roomRows.put(room.roomID(), roomRow);
	}

	public void updateContent(Collection<PublicRoom> rooms) {
		resetContent();
		for (PublicRoom room : rooms) putRoom(room);
	}

	public void resetContent() {
		scrollTable.clear();
		roomRows.clear();
	}
}
