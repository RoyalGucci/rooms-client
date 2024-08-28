package net.rooms.client.connection.requests;

public record ParticipationRequest(
		long id, // Game id
		String jSessionID
) {
}
