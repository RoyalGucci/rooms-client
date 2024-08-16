package net.rooms.client;

import net.rooms.client.connection.objects.Message;
import net.rooms.client.connection.objects.Participant;
import net.rooms.client.connection.objects.Room;

import java.util.HashMap;
import java.util.List;

/**
 * Holds all data associated with the user for the client.
 * All messages and room participants organized by rooms where the user is a participant.
 */
public class Repository {
	private final HashMap<Long, RoomEntry> localRepo;

	public Repository() {
		localRepo = new HashMap<>();
	}

	public void putEntry(RoomEntry entry) {
		localRepo.put(entry.room.roomID(), entry);
	}

	public RoomEntry getEntry(long roomID) {
		return localRepo.get(roomID);
	}

	public void clear() {
		localRepo.clear();
	}

	public record RoomEntry(
			Room room,
			List<Participant> participants,
			List<Message> messages
	) {
	}
}
