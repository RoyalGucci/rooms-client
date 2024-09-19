package net.rooms.client.games.pong.objects;

import java.util.List;

public record SendPong (
		List<SendPongEntry> entries,
		float ballX,
		float ballY
){
	public record SendPongEntry(
			String username,
			float location
	){
	}
}
