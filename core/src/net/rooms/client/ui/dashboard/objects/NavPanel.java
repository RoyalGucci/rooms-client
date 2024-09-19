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

		Texture texture = new Texture(Gdx.files.internal("door.png"));
		ImageButton logout = new ImageButton(new TextureRegionDrawable(texture));
		Texture texture2 = new Texture(Gdx.files.internal("search.png"));
		ImageButton search = new ImageButton(new TextureRegionDrawable(texture2));
		Texture texture3 = new Texture(Gdx.files.internal("dashboard.png"));
		ImageButton dashboard = new ImageButton(new TextureRegionDrawable(texture3));
		logout.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.getClient().getScreenManager().login();
				screen.getClient().getApiRequests().logout();
				screen.getClient().getRepository().clear();
			}
		});
		search.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.getClient().getScreenManager().search();
			}
		});
		dashboard.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.getClient().getScreenManager().dashboard();
			}
		});
		add(logout).pad(5).left();
		row();
		add(search).padTop(20).left();
		row();
		add(dashboard).padTop(20).padLeft(5).left();
		row();
	}
}
