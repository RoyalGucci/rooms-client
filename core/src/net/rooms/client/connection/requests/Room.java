package net.rooms.client.connection.requests;

public record Room(
        long roomID,
        String title,
        boolean isPrivate,
        String password,
        String owner,
        String creationDate,
        String description
) {
}