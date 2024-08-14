package net.rooms.client.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.rooms.client.connection.adapters.LocalDateTimeAdapter;
import net.rooms.client.connection.objects.Message;
import net.rooms.client.connection.objects.MessageType;
import net.rooms.client.connection.objects.Room;
import net.rooms.client.connection.requests.CreateRequest;
import net.rooms.client.connection.requests.SignupRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class APIRequests {

	private String jSessionID = "";
	@SuppressWarnings("FieldCanBeLocal")
	private final String domain = "http://localhost:8080/"; // TODO: load from file

	private WS ws;

	private HttpResponse<String> get(String endpoint, String[][] headers) {
		try (HttpClient client = HttpClient.newHttpClient()) {
			HttpRequest.Builder builder = HttpRequest.newBuilder();
			builder.uri(new URI(domain + endpoint));
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
			builder.uri(new URI(domain + endpoint));
			for (String[] header : headers)
				builder.header(header[0], header[1]);
			HttpRequest request = builder.POST(HttpRequest.BodyPublishers.ofString(body)).build();

			return client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (URISyntaxException | IOException | InterruptedException e) {
			return null;
		}
	}

	public boolean login(String username, String password) {
		String[][] headers = {{"Content-Type", "application/x-www-form-urlencoded"}};
		String body = "username=" + username + "&password=" + password;
		HttpResponse<String> response = post("login", headers, body);
		if (response == null) return false;

		String setCookieHeader = response.headers().firstValue("Set-Cookie").orElse("");
		jSessionID = setCookieHeader.split(";")[0]; // Extracts JSESSIONID
		if (jSessionID == null || jSessionID.isEmpty()) return false;

		ws = new WS(username, jSessionID);
		return true;
	}

	public void logout() {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		post("logout", headers, "");
	}

	public boolean signup(String nickname, String username, String password) {
		String[][] headers = {{"Content-Type", "application/json"}};
		String body = new Gson().toJson(new SignupRequest(nickname, username, password, 0));
		HttpResponse<String> response = post("api/v1/registration", headers, body);
		return response != null && response.body().equals("success");
	}

	public Room createRoom(String title, String description, boolean isPrivate, String password) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		String body = new Gson().toJson(new CreateRequest(title, description, isPrivate, password));
		HttpResponse<String> response = post("api/v1/room/create", headers, body);
		if (response == null) return null;

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.create();
		return gson.fromJson(response.body(), Room.class);
	}

	public List<Room> getRooms() {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		HttpResponse<String> response = get("api/v1/room/list", headers);
		if (response == null || response.body() == null || response.body().isEmpty()) return new ArrayList<>();

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.create();
		Room[] rooms = gson.fromJson(response.body(), Room[].class);
		return Arrays.asList(rooms);
	}

	public void message(long roomID, MessageType type, String content) {
		ws.message(roomID, type, content);
	}

	public List<Message> getMessages(long roomID) {
		String[][] headers = {{"Content-Type", "application/json"}, {"Cookie", jSessionID}};
		HttpResponse<String> response = get("messages/" + roomID, headers);
		if (response == null || response.body() == null || response.body().isEmpty()) return new ArrayList<>();

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.create();
		Message[] messages = gson.fromJson(response.body(), Message[].class);
		return Arrays.asList(messages);
	}

	/**
	 * Invokes the given operation upon receiving a payload over the web socket connection.
	 *
	 * @param listener The operation to invoke.
	 */
	public void setWSListener(Consumer<String> listener) {
		ws.setListener(listener);
	}
}
