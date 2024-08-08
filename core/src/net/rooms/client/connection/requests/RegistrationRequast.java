package net.rooms.client.connection.requests;

public record RegistrationRequast(
        String nickname,
        String username,
        String password,
        int role
) {
}
