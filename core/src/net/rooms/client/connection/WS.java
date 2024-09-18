package net.rooms.client.connection;

import net.rooms.client.JSON;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

class WS {

	private final String username;

	private final SessionHandler handler;
	private final HashMap<String, StompSession.Subscription> subscriptions;
	private Runnable onDisconnect;

	public WS(String username, String domain, String jSessionID) {
		this.username = username;

		List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new StringMessageConverter());

		handler = new SessionHandler();
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.add("Cookie", jSessionID);
		stompClient.connectAsync("ws://" + domain + "/ws", headers, handler);
		subscriptions = new HashMap<>();
	}

	public <T> void setWSListener(String destination, Consumer<T> consumer, Class<T> type) {
		String fullDestination = "/user/" + username + "/queue/" + destination;
		StompFrameHandler frameHandler = new StompFrameHandler() {
			@Override
			public @NonNull Type getPayloadType(@NonNull StompHeaders headers) {
				return String.class;
			}

			@Override
			public void handleFrame(@NonNull StompHeaders headers, Object payload) {
				T frame = JSON.fromJson((String) payload, type);
				consumer.accept(frame);
			}
		};
		if (handler.session == null) {
			handler.listenerQueue.add(new Object[]{fullDestination, frameHandler});
			return;
		}
		if (subscriptions.containsKey(destination)) subscriptions.get(destination).unsubscribe();
		subscriptions.put(destination, handler.session.subscribe(fullDestination, frameHandler));
	}

	public void removeWSListener(String destination) {
		if (!subscriptions.containsKey(destination)) return;

		subscriptions.remove(destination).unsubscribe();
	}

	public void setOnDisconnect(Runnable onDisconnect) {
		this.onDisconnect = onDisconnect;
	}

	public void send(String destination, String payload) {
		handler.send(destination, payload);
	}

	private class SessionHandler extends StompSessionHandlerAdapter {

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
			if (!session.isConnected()) {
				WS.this.onDisconnect.run();
				return;
			}
			session.send(destination, payload);
		}
	}
}
