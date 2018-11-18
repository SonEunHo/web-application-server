package webserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;

public class HttpRequest {
    private Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final HttpMethod method;
    private final String resource;
    private final String scheme;
    private final Map<String, String> headers;
    private final String body;
    private final Map<String, String> queryMap;
    private final Map<String, String> cookieMap;

    public HttpRequest(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        log.debug("\n\n-----------[read Request]");

        HttpMethod method = null;
        String resource = null;
        String scheme = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        String body = null;

        String line = null;

        try {
            line = br.readLine();
            log.debug(line);
            String[] temp = line.split(" ");
            method = HttpMethod.valueOf(temp[0]);
            resource = temp[1];
            scheme = temp[2];

            if(method.equals(HttpMethod.GET) && resource.equals("/"))
                resource = "/index.html";

            //read only HeaderSection
            while ((line = br.readLine()) != null) {
                log.debug(line);
                if (Strings.isNullOrEmpty(line)) break; //end of header section
                else {
                    Pair pair = HttpRequestUtils.parseHeader(line);
                    headers.put(pair.getKey(), pair.getValue());
                }
            }

            //read body if method is post
            if (method.equals(HttpMethod.POST)) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                body = IOUtils.readData(br, contentLength);
            }
        } catch (Exception e) {
            log.error("readRequest Error: {}", e.getMessage());
            throw new RuntimeException("readRequest Error");
        }

        this.method = method;
        this.resource = resource;
        this.scheme = scheme;
        this.headers = headers;
        this.body = body;
        if(resource.contains("?"))
            queryMap = HttpRequestUtils.parseQueryString(resource.split("\\?")[1]);
        else
            queryMap = null;
        cookieMap = HttpRequestUtils.parseCookies(headers.get("Cookie"));
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

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public String getCookie(String key) {
        return cookieMap.get(key);
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getQueryMap() {
        return queryMap;
    }

    public String getQuery(String key) {
        return queryMap.get(key);
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
