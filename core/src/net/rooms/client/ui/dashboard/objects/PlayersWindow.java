package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.rooms.client.ui.RoomsWindow;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class PlayersWindow extends RoomsWindow {
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
	}
}
