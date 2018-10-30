package webserver;

import java.util.Map;

public class HttpRequest {
    private final HttpMethod method;
    private final String resource;
    private final String scheme;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(HttpMethod method, String resource, String scheme,
                       Map<String, String> headers,String body) {
        this.method = method;
        this.resource = resource;
        this.scheme = scheme;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getResource() {
        return resource;
    }

    public String getScheme() {
        return scheme;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpRequest{");
        sb.append("method=").append(method);
        sb.append(", resource='").append(resource).append('\'');
        sb.append(", scheme='").append(scheme).append('\'');
        sb.append(", headers=").append(headers);
        sb.append(", body='").append(body).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
