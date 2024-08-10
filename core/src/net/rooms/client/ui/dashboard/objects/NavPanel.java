package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class NavPanel extends Table {
	public NavPanel(DashboardScreen screen) {
		Skin skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		top().left();
		setBackground(skin.newDrawable("white", 0.3f, 0.3f, 0.3f, 1));

		Texture texture = new Texture(Gdx.files.internal("settings-icon-size_32.png"));
		ImageButton logout = new ImageButton(new TextureRegionDrawable(texture));
		logout.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.getClient().getScreenManager().login();
			}
		});
		add(logout).pad(10).left();
		row();

	}
}
