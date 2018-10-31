package webserver;

import java.util.Map;

public class HttpResponse {
    private final HttpStatusCode statusCode;
    private final Map<String, String> headers;
    private final byte[] body;

    public HttpResponse(HttpStatusCode statusCode, Map<String, String> headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
