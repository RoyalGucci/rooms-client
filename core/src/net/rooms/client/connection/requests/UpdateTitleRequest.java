package net.rooms.client.connection.requests;

public record UpdateTitleRequest(
        long roomID,
        String title
) {
}