package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession {
    private final String id;
    private final Map<String, String> attributeMap;

    public HttpSession() {
        id = UUID.randomUUID().toString();
        attributeMap = new HashMap<>();
    }

    public String getAttribute(String key) {
        return attributeMap.get(key);
    }

    public void setAttribute(String key, String value) {
        attributeMap.put(key, value);
    }

    public void removeAttribute(String key) {
        attributeMap.remove(key);
    }

    public String getId() {
        return id;
    }
}
