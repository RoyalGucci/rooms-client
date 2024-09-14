package net.rooms.client.connection.adapters;

import com.google.gson.*;
import net.rooms.client.connection.objects.GameConfig;
import net.rooms.client.connection.objects.PongConfig;
import net.rooms.client.connection.objects.SnakesConfig;
import net.rooms.client.games.GameType;

import java.lang.reflect.Type;

public class ConfigAdapter implements JsonDeserializer<GameConfig> {

	Gson gson = new Gson();

	@Override
	public GameConfig deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		GameTypeViewer gameTypeViewer = gson.fromJson(jsonElement, GameTypeViewer.class);
		switch (gameTypeViewer.type) {
			case PONG -> {
				return gson.fromJson(jsonElement, PongConfig.class);
			}
			case SNAKES -> gson.fromJson(jsonElement, SnakesConfig.class);
		}
		return null;
	}

	private record GameTypeViewer(
			GameType type
	) {

	}
}