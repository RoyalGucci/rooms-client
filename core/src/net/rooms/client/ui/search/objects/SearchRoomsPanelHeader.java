package net.rooms.client.ui.search.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.rooms.client.ui.search.SearchScreen;

class SearchRoomsPanelHeader extends Table {

	private final TextField search;

	public SearchRoomsPanelHeader(SearchScreen screen) {
		Skin skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		top().left();
		setBackground(skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1));
		search = new TextField("", skin);
		search.setMessageText("Search");
		add(search).pad(5).expandX().fillX().colspan(2);
		row();

		search.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!search.getText().isEmpty())
					screen.setSearchedRooms(screen.getClient().getApiRequests().searchPublicRooms(search.getText()));
			}
		});
	}

	public void resetContent() {
		search.setText("");
	}
}
