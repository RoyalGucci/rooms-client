package net.rooms.client.connection.requests;

public record UpdateDescriptionRequest(
        long roomID,
        String description
) {
}