package db;

import java.util.HashMap;
import java.util.Map;

import webserver.HttpSession;

public class SessionDB {
    private static final Map<String, HttpSession> sessionMap = new HashMap<>();;

//    public SessionDB() {
//        this.sessionMap = new HashMap<>();
//    }

    public static HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public static void saveSession(HttpSession httpSession) {
        sessionMap.put(httpSession.getId(), httpSession);
    }

    public static void removeSession(String id) {
        sessionMap.remove(id);
    }
}
