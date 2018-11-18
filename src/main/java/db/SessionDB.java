package db;

import java.util.HashMap;
import java.util.Map;

import webserver.HttpSession;

public class SessionDB {
    private final Map<String, HttpSession> sessionMap;

    public SessionDB() {
        this.sessionMap = new HashMap<>();
    }

    public HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public void saveSession(HttpSession httpSession) {
        sessionMap.put(httpSession.getId(), httpSession);
    }
}
