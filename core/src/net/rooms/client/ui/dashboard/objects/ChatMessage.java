package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class ChatMessage extends Table {

	public ChatMessage(String message, String username, Skin skin) {
		Label messageBody = new Label(" " + message, skin);
		Label author = new Label(" " + username, skin);
		messageBody.setWrap(true);
		setBackground(skin.newDrawable("white", 0.7f, 0.7f, 0.7f, 1));

		add(author).expandX().left().fillX();
		row();
		add(messageBody).expandX().left().fillX();
	}
}
