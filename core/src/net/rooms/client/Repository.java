package net.rooms.client;

import net.rooms.client.connection.objects.Message;
import net.rooms.client.connection.objects.Participant;
import net.rooms.client.connection.objects.Room;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Holds all data associated with the user for the client.
 * All messages and room participants organized by rooms where the user is a participant.
 */
public class Repository {
	private final HashMap<Long, RoomEntry> localRepo; // Key: roomID

	public Repository() {
		localRepo = new HashMap<>();
	}

	public void putEntry(RoomEntry entry) {
		localRepo.put(entry.room.roomID(), entry);
	}

	public RoomEntry getEntry(long roomID) {
		return localRepo.get(roomID);
	}

	public void removeEntry(long roomID) {
		localRepo.remove(roomID);
	}

	public Collection<RoomEntry> listEntries() {
		return localRepo.values();
	}

	public void clear() {
		localRepo.clear();
	}

	public record RoomEntry(
			Room room,
			HashMap<String, Participant> participants, // Key: username
			HashMap<Long, Message> messages
	) {
		public RoomEntry(Room room, List<Participant> participants, List<Message> messages) {
			this(room, hashParticipants(participants), hashMessages(messages));
		}

		public List<Message> getMessagesSortedByDate() {
			return messages.values().stream().sorted(Comparator.comparing(Message::sendDate)).toList();
		}

		private static HashMap<String, Participant> hashParticipants(List<Participant> participants) {
			HashMap<String, Participant> participantHashMap = new HashMap<>(participants.size());
			for (Participant participant : participants) participantHashMap.put(participant.username(), participant);
			return participantHashMap;
		}

		private static HashMap<Long, Message> hashMessages(List<Message> messages) {
			HashMap<Long, Message> messageHashMap = new HashMap<>(messages.size());
			for (Message message : messages) messageHashMap.put(message.id(), message);
			return messageHashMap;
		}
	}
}
