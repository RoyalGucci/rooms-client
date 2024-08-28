package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class ChatGameMessage extends Table {

	public ChatGameMessage(String message, String username,String gameName, boolean me, Skin skin) {
		Label amountConnected = new Label(" " + message, skin);
		Label author = new Label(" " + username, skin);
		Texture texture = new Texture(Gdx.files.internal(gameName +".png"));
		ImageButton gamePicture = new ImageButton(new TextureRegionDrawable(texture));
		TextButton start = new TextButton("Start", skin);
		TextButton leave = new TextButton("Leave", skin);
		TextButton join = new TextButton("Join", skin);
		setBackground(skin.newDrawable("white", 0.7f, 0.7f, 0.7f, 1));
		if (me) {
			add(author).expandX().left().fillX();
			row();
			add(gamePicture).left();
			add(amountConnected).left();
			add(start).right();
			add(leave).right();

		} else {
			add(author).expandX().left().fillX();
			row();
			add(gamePicture).expandX().left().fillX();
			add(amountConnected).expandX().left().fillX();
			add(join).expandX().right().fillX();
		}
	}
}