package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.rooms.client.Repository;
import net.rooms.client.ui.dashboard.DashboardScreen;

import java.util.List;
import java.util.stream.Collectors;

class RoomsPanelHeader extends Table {

	private final TextField search;

	public RoomsPanelHeader(DashboardScreen screen) {
		Skin skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		top().left();
		setBackground(skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1));

		add(new Label("            My Rooms", skin)).pad(10).expandX(); // Title
		Texture texture = new Texture(Gdx.files.internal("plus.png"));
		ImageButton create = new ImageButton(new TextureRegionDrawable(texture));
		create.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getStage().addActor(new CreateRoomDialog(screen, skin));
			}
		});
		add(create).pad(10).left();
		row();
		search = new TextField("", skin);
		search.setMessageText("Search");
		add(search).pad(5).expandX().fillX().colspan(2);
		row();

		search.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				List<Repository.RoomEntry> filteredRooms = screen.getRooms().stream()
						.filter(room -> room.room().title().startsWith(search.getText()))
						.collect(Collectors.toList());
				screen.setSearchedRooms(filteredRooms);
			}
		});
	}

	public void resetContent() {
		search.setText("");
	}
}
