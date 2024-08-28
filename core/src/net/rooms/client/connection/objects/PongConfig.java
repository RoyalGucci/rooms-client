package net.rooms.client.connection.objects;

/**
 * Represents a configuration for the game of 'Pong'.
 *
 * @param maxPlayers The maximum number of players that may participate in the game.
 * @param winScore   The score to win the game. For games with no score this property should be set
 *                   to -1.
 */
public record PongConfig(
		int maxPlayers,
		int winScore
) implements GameConfig {
}
