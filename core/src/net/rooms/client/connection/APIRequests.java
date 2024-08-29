package net.rooms.client.connection;

import net.rooms.client.JSON;
import net.rooms.client.connection.objects.Message;
import net.rooms.client.connection.objects.MessageType;
import net.rooms.client.connection.objects.Participant;
import net.rooms.client.connection.objects.PublicRoom;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.connection.requests.BroadcastRequest;
import net.rooms.client.connection.requests.CreateRequest;
import net.rooms.client.connection.requests.InviteRequest;
import net.rooms.client.connection.requests.JoinRequest;
import net.rooms.client.connection.requests.LeaveRoom;
import net.rooms.client.connection.requests.MessageRequest;
import net.rooms.client.connection.requests.ParticipationRequest;
import net.rooms.client.connection.requests.SignupRequest;
import net.rooms.client.connection.requests.UpdateDescriptionRequest;
import net.rooms.client.connection.requests.UpdateTitleRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class APIRequests {

	private String jSessionID = "";
	private String username = "";
	private final String domain;
	private final String url;

	private WS ws;

	public APIRequests() {
		String domain;
		File configFile = new File("config.cfg");
		try {
			Scanner scanner = new Scanner(configFile);
			domain = scanner.next();
			scanner.close();
		} catch (FileNotFoundException e) {
			domain = "localhost:8080";
		}
		this.domain = domain;
		url = "http://" + domain + "/";
	}

	public String getUsername() {
		return username;
	}

	private HttpResponse<String> get(String endpoint, String[][] headers) {
		try (HttpClient client = HttpClient.newHttpClient()) {
			HttpRequest.Builder builder = HttpRequest.newBuilder();
			builder.uri(new URI(url + endpoint));
			for (String[] header : headers)
				builder.header(header[0], header[1]);
			HttpRequest request = builder.build();

			return client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (URISyntaxException | IOException | InterruptedException e) {
			return null;
		}
	}

	private HttpResponse<String> post(String endpoint, String[][] headers, String body) {
		try (HttpClient client = HttpClient.newHttpClient()) {
			HttpRequest.Builder builder = HttpRequest.newBuilder();
			builder.uri(new URI(url + endpoint));
			for (String[] header : headers)
				builder.header(header[0], header[1]);
			HttpRequest request = builder.POST(HttpRequest.BodyPublishers.ofString(body)).build();

			return client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (URISyntaxException | IOException | InterruptedException e) {
			return null;
		}
	}

	public boolean login(String username, String password) {
		this.username = username;
		String[][] headers = {{"Content-Type", "application/x-www-form-urlencoded"}};
		String body = "username=" + username + "&password=" + password;
		HttpResponse<String> response = post("login", headers, body);
		if (response == null) return false;

		String setCookieHeader = response.headers().firstValue("Set-Cookie").orElse("");
		jSessionID = setCookieHeader.split(";")[0]; // Extracts JSESSIONID
		if (jSessionID == null || jSessionID.isEmpty()) return false;

		headers = new String[][]{{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		response = get("api/v1/user/verify", headers); // No such endpoint, 404 expected
		if (response == null || response.body() == null || response.body().isEmpty())
			return false; // No 404, auth failed

		ws = new WS(username, domain, jSessionID);
		return true;
	}

	public void logout() {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		post("logout", headers, "");
	}

	public boolean signup(String nickname, String username, String password) {
		String[][] headers = {{"Content-Type", "application/json"}};
		String body = JSON.toJson(new SignupRequest(nickname, username, password, 0));
		HttpResponse<String> response = post("api/v1/registration", headers, body);
		return response != null && response.body().equals("success");
	}

	public Room createRoom(String title, String description, boolean isPrivate, String password) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		String body = JSON.toJson(new CreateRequest(title, description, isPrivate, password));
		HttpResponse<String> response = post("api/v1/room/create", headers, body);
		if (response == null) return null;

		return JSON.fromJson(response.body(), Room.class);
	}

	public List<Room> getRooms() {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		HttpResponse<String> response = get("api/v1/room/list", headers);
		if (response == null || response.body() == null || response.body().isEmpty()) return new ArrayList<>();

		Room[] rooms = JSON.fromJson(response.body(), Room[].class);
		return Arrays.asList(rooms);
	}

	public List<PublicRoom> searchPublicRooms(String titlePrefix) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		HttpResponse<String> response = get("api/v1/room/search/" + titlePrefix, headers);
		if (response == null || response.body() == null || response.body().isEmpty()) return new ArrayList<>();

		PublicRoom[] rooms = JSON.fromJson(response.body(), PublicRoom[].class);
		return Arrays.asList(rooms);
	}

	public List<Participant> getParticipants(long roomID) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		HttpResponse<String> response = get("api/v1/room/" + roomID + "/participants", headers);
		if (response == null || response.body() == null || response.body().isEmpty()) return new ArrayList<>();

		Participant[] participants = JSON.fromJson(response.body(), Participant[].class);
		return Arrays.asList(participants);
	}

	public boolean joinRoom(long roomID, String password) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		String body = JSON.toJson(new JoinRequest(roomID, password));
		HttpResponse<String> response = post("api/v1/room/join", headers, body);
		return response != null && response.body().equals("success");
	}

	public boolean inviteToRoom(long roomID, String username) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		String body = JSON.toJson(new InviteRequest(roomID, username));
		HttpResponse<String> response = post("api/v1/room/invite", headers, body);
		return response != null && response.body().equals("success");
	}

	public boolean leaveRoom(long roomID) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		String body = JSON.toJson(new LeaveRoom(roomID));
		HttpResponse<String> response = post("api/v1/room/leave", headers, body);
		return response != null && response.body().equals("success");
	}

	public boolean updateTitle(long id, String title) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		String body = JSON.toJson(new UpdateTitleRequest(id, title));
		HttpResponse<String> response = post("api/v1/room/update/title", headers, body);
		return response != null && response.body().equals("success");
	}

	public boolean updateDescription(long id, String description) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		String body = JSON.toJson(new UpdateDescriptionRequest(id, description));
		HttpResponse<String> response = post("api/v1/room/update/description", headers, body);
		return response != null && response.body().equals("success");
	}

	public void message(long roomID, MessageType type, String content) {
		MessageRequest messageRequest = new MessageRequest(roomID, type, content, jSessionID);
		ws.send("/app/message", JSON.toJson(messageRequest));
	}

	/**
	 * Can be used to send a request to join a game lobby.
	 * Successful requests would trigger an update on the game/leave endpoint to all participants of
	 * the room where the lobby is present.
	 *
	 * @param id The identifier of the game.
	 */
	public void joinGame(long id) {
		ParticipationRequest participationRequest = new ParticipationRequest(id, jSessionID);
		ws.send("/game/join", JSON.toJson(participationRequest));
	}

	/**
	 * Can be used to send a request to leave a game lobby.
	 * Successful requests would trigger an update on the game/leave endpoint to all participants
	 * of the room where the lobby is present.
	 * Can also be used to quit an active game.
	 *
	 * @param id The identifier of the game.
	 */
	public void leaveGame(long id) {
		ParticipationRequest participationRequest = new ParticipationRequest(id, jSessionID);
		ws.send("/game/leave", JSON.toJson(participationRequest));
	}

	/**
	 * Used by a game host to start a pending game.
	 * Successful requests would trigger an update on the game/start endpoint to all participants
	 * of the room where the lobby is present.
	 *
	 * @param id The identifier of the game.
	 */
	public void startGame(long id) {
		ParticipationRequest participationRequest = new ParticipationRequest(id, jSessionID);
		ws.send("/game/start", JSON.toJson(participationRequest));
	}

	/**
	 * Used by the host to send a payload to all players in a hosted game.
	 * Will be received by players in game/guest-channel
	 *
	 * @param id      The identifier of the hosted game.
	 * @param payload The payload to transmit. Expected to represent the json of any object.
	 */
	public void sendToGameGuests(long id, String payload) {
		BroadcastRequest broadcastRequest = new BroadcastRequest(id, payload, jSessionID);
		ws.send("/game/broadcast", JSON.toJson(broadcastRequest));
	}

	/**
	 * Used by guests to send a payload to the host of a game.
	 * Will be received by players in game/host-channel
	 *
	 * @param id      The identifier of the game.
	 * @param payload The payload to transmit. Expected to represent the json of any object.
	 */
	public void sendToGameHost(long id, String payload) {
		BroadcastRequest broadcastRequest = new BroadcastRequest(id, payload, jSessionID);
		ws.send("/game/unicast", JSON.toJson(broadcastRequest));
	}

	public List<Message> getMessages(long roomID) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		HttpResponse<String> response = get("messages/" + roomID, headers);
		if (response == null || response.body() == null || response.body().isEmpty()) return new ArrayList<>();

		Message[] messages = JSON.fromJson(response.body(), Message[].class);
		return Arrays.asList(messages);
	}

	/**
	 * Subscribes the session to the given web socket endpoint.
	 * Registers the given operation to be invoked upon receiving a payload over the web socket
	 * connection.
	 * If the session is already subscribed to the given endpoint it will not subscribe again but,
	 * the given operation would override the previous one.
	 *
	 * @param destination Indicate which endpoint to listen (e.g. messages).
	 * @param consumer    The operation to invoke.
	 * @param type        The expected type of the object in the payload.
	 */
	public <T> void setWSListener(String destination, Consumer<T> consumer, Class<T> type) {
		ws.setWSListener(destination, consumer, type);
	}

	/**
	 * Unsubscribes the session from the given websocket endpoint.
	 * The operation assigned to that endpoint would be unassigned.
	 * If the session is not subscribed to the given endpoint, nothing would happen.
	 *
	 * @param destination The websocket endpoint to unsubscribe from.
	 */
	public void removeWSListener(String destination) {
		ws.removeWSListener(destination);
	}
}
