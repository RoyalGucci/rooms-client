package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.rooms.client.ui.dashboard.DashboardScreen;

class RoomsPanelHeader extends Table {

	private final TextField search;

	public RoomsPanelHeader(DashboardScreen screen) {
		Skin skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		top().left();
		setBackground(skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1));

		add(new Label("Rooms", skin)).pad(10).expandX(); // Title
		Texture texture = new Texture(Gdx.files.internal("settings-icon-size_32.png"));
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
		add(search).pad(5).expandX().fillX();
		add(new TextButton("ok", skin)).pad(5);
		row();
	}

	public void resetContent() {
		search.setText("");
	}
}
