package net.rooms.client.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.rooms.client.connection.adapters.LocalDateTimeAdapter;
import net.rooms.client.connection.objects.MessageType;
import net.rooms.client.connection.requests.MessageRequest;
import net.rooms.client.connection.requests.ParticipationRequest;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

class WS {

	@SuppressWarnings("FieldCanBeLocal")
	private final String url;
	private final String username;
	private final String jSessionID;

	private final SessionHandler handler;
	private final Gson gson;

	public WS(String domain, String username, String jSessionID) {
		url = "ws://" + domain + "/ws";
		this.username = username;
		this.jSessionID = jSessionID;

		List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new StringMessageConverter());

		handler = new SessionHandler();
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.add("Cookie", jSessionID);
		stompClient.connectAsync(url, headers, handler);

		gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.create();
	}

	public <T> void addWSListener(String destination, Consumer<T> consumer, Type type) {
		destination = "/user/" + username + "/queue/" + destination;
		StompFrameHandler frameHandler = new StompFrameHandler() {
			@Override
			public @NonNull Type getPayloadType(@NonNull StompHeaders headers) {
				return String.class;
			}

			@Override
			public void handleFrame(@NonNull StompHeaders headers, Object payload) {
				T frame = gson.fromJson((String) payload, type);
				consumer.accept(frame);
			}
		};
		if (handler.session == null) {
			handler.listenerQueue.add(new Object[]{destination, frameHandler});
			return;
		}
		handler.session.subscribe(destination, frameHandler);
	}

	public void message(long roomID, MessageType type, String content) {
		MessageRequest messageRequest = new MessageRequest(roomID, type, content, jSessionID);
		handler.send("/app/message", gson.toJson(messageRequest));
	}

	public void joinGame(long id) {
		ParticipationRequest participationRequest = new ParticipationRequest(id, jSessionID);
		handler.send("/game/join", gson.toJson(participationRequest));
	}

	public void leaveGame(long id) {
		ParticipationRequest participationRequest = new ParticipationRequest(id, jSessionID);
		handler.send("/game/leave", gson.toJson(participationRequest));
	}

	private static class SessionHandler extends StompSessionHandlerAdapter {

		StompSession session;
		Queue<String[]> sendQueue = new LinkedList<>();
		Queue<Object[]> listenerQueue = new LinkedList<>();

		@Override
		public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
			this.session = session;

			for (String[] request : sendQueue) send(request[0], request[1]);
			sendQueue.clear();
			for (Object[] listener : listenerQueue)
				session.subscribe((String) listener[0], (StompFrameHandler) listener[1]);
			listenerQueue.clear();
		}

		public void send(String destination, String payload) {
			if (session == null) {
				sendQueue.add(new String[]{destination, payload});
				return;
			}
			session.send(destination, payload);
		}
	}
}
