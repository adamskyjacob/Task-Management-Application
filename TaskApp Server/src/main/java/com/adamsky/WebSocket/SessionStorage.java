package com.adamsky.WebSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {

    private final Map<String, WebSocketSession> userSessions;

    public SessionStorage() {
        userSessions = new ConcurrentHashMap<>();
    }

    public void addUserSession(String userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    public WebSocketSession getUserSession(String userId) {
        return userSessions.get(userId);
    }

    public WebSocketSession removeUserSession(String userId) {
        return userSessions.remove(userId);
    }
}
