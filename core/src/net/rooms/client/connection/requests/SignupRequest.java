package net.rooms.client.connection.requests;

public record SignupRequest(
		String nickname,
		String username,
		String password,
		int role
) {
}
