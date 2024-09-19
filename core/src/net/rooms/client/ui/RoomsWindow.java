package net.rooms.client.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RoomsWindow extends Window {
	public RoomsWindow(String title, Skin skin) {
		super(title, skin);

		TextButton close = new TextButton("X", skin);
		close.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				RoomsWindow.this.remove();
			}
		});
		getTitleTable().add(close).padLeft(10);
	}
}
