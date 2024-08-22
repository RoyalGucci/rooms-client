package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class PlayersWindow extends Window {
	public PlayersWindow(DashboardScreen screen, Skin skin) {
		super("Players", skin);
		setSize(150, 300);
		setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
		Table table = new Table();

		screen.getRoom(screen.currentRoomID).participants().values().forEach(participant ->
				table.add(new Label(participant.nickname(), skin)).row());

		// Create a scroll pane and add the table to it
		ScrollPane scrollPane = new ScrollPane(table, skin);

		// Add the scroll pane to the window
		this.add(scrollPane).expand().fill();

		TextButton closeButton = new TextButton("X", skin);
		closeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				PlayersWindow.this.remove();
			}
		});

		// Add the close button to the window's title table
		this.getTitleTable().add(closeButton).padLeft(10);
	}
}
