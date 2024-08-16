package net.rooms.client.connection;

import com.google.gson.Gson;
import net.rooms.client.connection.objects.MessageType;
import net.rooms.client.connection.requests.MessageRequest;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

class WS {

	@SuppressWarnings("FieldCanBeLocal")
	private final String url = "ws://localhost:8080/ws"; // TODO: load from file
	private final String username;
	private final String jSessionID;

	private final SessionHandler handler;

	public WS(String username, String jSessionID) {
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
	}

	public void setListener(Consumer<String> consumer) {
		handler.consumer = consumer;
	}

	public void message(long roomID, MessageType type, String content) {
		MessageRequest messageRequest = new MessageRequest(roomID, type, content, jSessionID);
		handler.send("/app/message", new Gson().toJson(messageRequest));
	}

	private class SessionHandler extends StompSessionHandlerAdapter {

		StompSession session;
		Consumer<String> consumer = s -> {
		};
		Queue<String[]> queue = new LinkedList<>();

		@Override
		public void afterConnected(StompSession session, @NonNull StompHeaders connectedHeaders) {
			this.session = session;
			session.subscribe("/user/" + username + "/queue/messages", this);
			for (String[] request : queue) send(request[0], request[1]);
			queue.clear();
		}

		public void send(String destination, String payload) {
			if (session == null) {
				queue.add(new String[]{destination, payload});
				return;
			}
			session.send(destination, payload);
		}

		@Override
		public void handleFrame(@NonNull StompHeaders headers, Object payload) {
			consumer.accept((String) payload);
		}
	}
}
