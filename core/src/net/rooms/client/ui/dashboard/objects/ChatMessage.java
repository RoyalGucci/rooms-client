package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class ChatMessage extends Table {
	// TODO: add sender details
	public ChatMessage(String message, Skin skin) {
		Label messageLabel = new Label(message, skin);
		messageLabel.setWrap(true);
		add(messageLabel).expandX().fillX();
	}
}
