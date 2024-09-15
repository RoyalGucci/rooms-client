package net.rooms.client.games.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.rooms.client.Client;
import net.rooms.client.connection.objects.GameUpdate;
import net.rooms.client.connection.objects.PongConfig;
import net.rooms.client.games.GameScreen;
import net.rooms.client.games.pong.helper.ColorTypes;
import net.rooms.client.games.pong.helper.ContactType;
import net.rooms.client.games.pong.objects.Ball;
import net.rooms.client.games.pong.objects.Player;
import net.rooms.client.games.pong.objects.Wall;

import java.util.*;

public class PongScreen extends GameScreen {

	private final Viewport viewport;
	public static final int PONG_SCREEN_SIZE = 800;
	public final String participant;
	private final String host;
	public final int winScore;
	protected int playerNumber;
	protected final HashMap<String, Player> players; // Usernames to player objects
	protected final HashMap<Wall.WallSide, Player> playersBySide;
	protected final HashMap<Wall.WallSide, Wall> walls;
	protected final Ball ball;
	protected final Player empty;
	protected final PongConfig config;
	protected final List<String> participants;
	private final BitmapFont topLeftFont;
	private final BitmapFont topRightFont;
	private final BitmapFont bottomLeftFont;
	private final BitmapFont bottomRightFont;
	private final GlyphLayout glyphLayoutTopLeft;
	private final GlyphLayout glyphLayoutTopRight;
	private final GlyphLayout glyphLayoutBottomLeft;
	private final GlyphLayout glyphLayoutBottomRight;


	public PongScreen(Client client, GameUpdate update, long gameID, String participant, String host) {
		super(client, update, gameID);
		this.participant = participant;
		this.host = host;
		config = (PongConfig) update.config();
		winScore = config.winScore();
		empty = new Player(0, 0, this, "", ContactType.VERTICAL_PLAYER, ColorTypes.GREEN);
		empty.setActive(false);
		viewport = new FitViewport(PONG_SCREEN_SIZE, PONG_SCREEN_SIZE);

		playerNumber = update.participants().size();
		players = new HashMap<>(playerNumber);
		playersBySide = new HashMap<>();
		float[][] sides = new float[][]{{16, (float) PONG_SCREEN_SIZE / 2},
				{PONG_SCREEN_SIZE - 16, (float) PONG_SCREEN_SIZE / 2},
				{(float) PONG_SCREEN_SIZE / 2, 16},
				{(float) PONG_SCREEN_SIZE / 2, PONG_SCREEN_SIZE - 16}};
		ContactType[] contactTypes = new ContactType[]{ContactType.VERTICAL_PLAYER, ContactType.VERTICAL_PLAYER, ContactType.HORIZONTAL_PLAYER, ContactType.HORIZONTAL_PLAYER};
		ColorTypes[] colorTypes = new ColorTypes[]{ColorTypes.BLUE, ColorTypes.RED, ColorTypes.GREEN, ColorTypes.GOLD};
		Wall.WallSide[] wallSides = new Wall.WallSide[]{Wall.WallSide.LEFT, Wall.WallSide.RIGHT, Wall.WallSide.BOTTOM, Wall.WallSide.TOP};
		participants = update.participants();
		for (int i = 0; i < participants.size(); i++) {
			String username = participants.get(i);
			Player player = new Player(sides[i][0], sides[i][1], this, username, contactTypes[i],colorTypes[i]);
			players.put(username, player);
			player.setAlive(true);
			playersBySide.put(wallSides[i], player);
		}

		walls = new HashMap<>();

		int[] wallLocations = new int[]{0, PONG_SCREEN_SIZE, 0, PONG_SCREEN_SIZE};
		for (int i = 0; i < 4; i++) {
			Player player = players.getOrDefault(getOrDefault(participants, i), empty);
			walls.put(wallSides[i], new Wall(wallLocations[i], this, player, wallSides[i]));
		}

		ball = new Ball(this);
		glyphLayoutTopLeft = new GlyphLayout();
		glyphLayoutTopRight = new GlyphLayout();
		glyphLayoutBottomLeft = new GlyphLayout();
		glyphLayoutBottomRight = new GlyphLayout();
		topLeftFont = new BitmapFont();
		topRightFont = new BitmapFont();
		bottomLeftFont = new BitmapFont();
		bottomRightFont = new BitmapFont();
		topLeftFont.setColor(Color.GREEN);
		topRightFont.setColor(Color.GREEN);
		bottomLeftFont.setColor(Color.GREEN);
		bottomRightFont.setColor(Color.GREEN);

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true); // Maintain aspect ratio
	}

	@Override
	public void update() {
		super.update();
		Player player;
		player = playersBySide.get(Wall.WallSide.LEFT);
		glyphLayoutTopLeft.setText(topLeftFont, player.getUsername() + " = " + player.getScore());
		if(participants.size() > 3) {
			player = playersBySide.get(Wall.WallSide.TOP);
			glyphLayoutTopRight.setText(topRightFont, player.getUsername() + " = " + player.getScore());
		}
		if(participants.size() > 2) {
			player = playersBySide.get(Wall.WallSide.BOTTOM);
			glyphLayoutBottomLeft.setText(bottomLeftFont, player.getUsername() + " = " + player.getScore());
		}
		player = playersBySide.get(Wall.WallSide.RIGHT);
		glyphLayoutBottomRight.setText(bottomRightFont, player.getUsername() + " = " + player.getScore());

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) client.getApiRequests().leaveGame(gameID);
	}

	private void quit() {
		client.getScreenManager().dashboard();
		dispose();
	}

	@Override
	public void render(float delta) {
		update();
		super.render();
		viewport.apply();


		batch.begin();
		ball.render(batch);
		players.values().forEach(player -> player.render(batch));
		walls.values().forEach(wall -> wall.render(batch));
		topLeftFont.draw(batch, glyphLayoutTopLeft, 40, PONG_SCREEN_SIZE - 40);
		topRightFont.draw(batch, glyphLayoutTopRight, PONG_SCREEN_SIZE - 80, PONG_SCREEN_SIZE - 40);
		bottomLeftFont.draw(batch, glyphLayoutBottomLeft, 40, 40);
		bottomRightFont.draw(batch, glyphLayoutBottomRight, PONG_SCREEN_SIZE - 80, 40);
		batch.end();
	}

	public void reset() {
		for (Map.Entry<Wall.WallSide, Player> entry : playersBySide.entrySet()) {
			Player player = entry.getValue();
			player.setAlive(true);
			player.setActive(true);
			player.update((float) PONG_SCREEN_SIZE / 2);
			walls.get(entry.getKey()).setActive(false);
		}
		ball.setDead();
	}

	@Override
	public void onDisconnect(String username) {
		playerNumber--;
		if (!host.equals(username) && playerNumber <= 1) client.getApiRequests().leaveGame(gameID); // Host must close the game
		if (participant.equals(username) || host.equals(username) || playerNumber <= 1) {
			quit();
			return;
		}
		Player player = getPlayer(username);
		player.setAlive(false);
		player.setActive(false);
		Wall.WallSide foundKey = playersBySide.entrySet().stream().filter(entry -> entry.getValue().getUsername().equals(username)).findFirst().map(Map.Entry::getKey).orElse(null);
		players.remove(username);
		playersBySide.remove(foundKey);
		reset();
	}

	public void winner(String username) {
		ball.setActive(false);
	}

	private String getOrDefault(List<String> list, int i) {
		if (list.size() > i) return list.get(i);
		return "";
	}

	public Player getPlayer(String username) {
		return players.getOrDefault(username, empty);
	}

	public Player getPlayer(Wall.WallSide wallSide) {
		return playersBySide.getOrDefault(wallSide, empty);
	}

	public Collection<Player> getPlayers() {
		return players.values();
	}

	public Wall getWall(Wall.WallSide wallSide) {
		return walls.get(wallSide);
	}

	public Ball getBall() {
		return ball;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}
}
