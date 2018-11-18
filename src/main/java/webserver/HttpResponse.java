package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private HttpStatusCode statusCode;
    private Map<String, String> headers;
    private byte[] body;
    private DataOutputStream dos;

    public HttpResponse(HttpStatusCode statusCode, Map<String, String> headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public HttpResponse(OutputStream outputStream) {
        this(new DataOutputStream(outputStream));
    }

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public HttpResponse(HttpStatusCode statusCode, Map<String, String> headers, byte[] body,
                        DataOutputStream dos) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.dos = dos;
    }

    public void sendResponse() {
        try {
            dos.writeBytes("HTTP/1.1 "+getStatusCode().getStatusCode()+" "+getStatusCode().getMessage()+"\r\n");
            getHeaders().forEach((k,v) -> {
                try {
                    dos.writeBytes(k + ": " + v+"\r\n");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });
            if(getBody() != null) {
                dos.writeBytes("Content-Length: "+getBody().length+"\r\n");
                dos.writeBytes("\r\n");
                dos.write(getBody(), 0, getBody().length);
            } else {
                dos.writeBytes("\r\n");
            }
            dos.flush();
            log.debug("[Send Response] statusCode = {}, headers = {}", statusCode, headers);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendErrorResponse(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        addHeader("Content-Type", "text/html;charset=utf-8");
        sendResponse();
    }

    public void sendRedirect(String location) {
        statusCode = HttpStatusCode.REDIRECT;
        addHeader("Location", location);
        sendResponse();
    }

    public void addHeader(String header, String value) {
        if(headers == null) {
            headers = new HashMap<>();
        }
        headers.put(header, value);
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(byte[] body) {
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
