package net.rooms.client.connection.objects;

public record PongResults(
		String winner
) {
	@Override
	public String toString() {
		return "WINNER: " + winner;
	}
}
