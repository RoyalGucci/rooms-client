package net.rooms.client.connection.requests;

public record CreateRequest(
        String title,
        boolean isPrivate,
        String password,
        String description
) {
}