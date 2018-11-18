package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import db.SessionDB;

public class HttpSession {
    private final String id;
    private final Map<String, Object> attributeMap;
    private final Map<String, Object> statusMap;

    public HttpSession() {
        id = UUID.randomUUID().toString();
        attributeMap = new HashMap<>();
        statusMap = new HashMap<>();
    }

    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributeMap.put(key, value);
    }

    public void removeAttribute(String key) {
        attributeMap.remove(key);
    }

    public String getId() {
        return id;
    }

    public void invalidate() {
        SessionDB.removeSession(id);
    }
}
