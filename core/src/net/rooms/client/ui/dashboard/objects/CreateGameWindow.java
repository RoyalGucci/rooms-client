package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.rooms.client.JSON;
import net.rooms.client.connection.objects.MessageType;
import net.rooms.client.connection.objects.PongConfig;
import net.rooms.client.games.GameType;
import net.rooms.client.ui.RoomsWindow;
import net.rooms.client.ui.ScrollListener;
import net.rooms.client.ui.dashboard.DashboardScreen;

public class CreateGameWindow extends RoomsWindow {
	private String selectedGame = "Pong"; // TODO try to use MessageType enum properties instead
	private int playerNum = 2;

	public CreateGameWindow(DashboardScreen screen, Skin skin) {
		super("Create a game", skin);
		setSize(350, 300);
		setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
		Table table = new Table();
		table.addListener(new ScrollListener(table));

		TextButton createButton = new TextButton("Create", skin);
		table.add(new Label("Choose a game:", skin));
		SelectBox<String> gamesDropdown = new SelectBox<>(skin); // 'skin' is your UI skin
		String[] options = {"Pong", "Snakes", "Tanks"};
		gamesDropdown.setItems(options);
		table.add(gamesDropdown).pad(20).row();
		table.add(new Label("Choose amount of players:", skin));
		SelectBox<String> playerNumDropdown = new SelectBox<>(skin);
		String[] num = {"2", "3", "4"};
		playerNumDropdown.setItems(num);
		table.add(playerNumDropdown).pad(20).row();
		table.add(createButton).pad(20).colspan(2);


		gamesDropdown.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectedGame = gamesDropdown.getSelected();
			}
		});

		playerNumDropdown.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				playerNum = Integer.parseInt(playerNumDropdown.getSelected());
			}
		});

		createButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (selectedGame.equals("Pong"))
					// TODO handle winScore
					screen.getClient().getApiRequests().message(screen.currentRoomID, MessageType.PONG_GAME_OPEN, JSON.toJson(new PongConfig(GameType.PONG, playerNum, -1)));
				// TODO handle more games
				remove();
			}
		});

		// Create a scroll pane and add the table to it
		ScrollPane scrollPane = new ScrollPane(table, skin);

		// Add the scroll pane to the window
		this.add(scrollPane).expand().fill();
	}
}
