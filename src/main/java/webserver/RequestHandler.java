package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import service.SignUpServiceImpl;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private ResourceHandler resourceHandler;
    private SignUpHandler signUpHandler;
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        resourceHandler = new ResourceHandler();
        signUpHandler = new SignUpHandler(SignUpServiceImpl.getService());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = readRequest(new BufferedReader(new InputStreamReader(in)));
            log.debug("http request: {}", httpRequest);

            byte[] body;
            String resource = httpRequest.getResource();
            DataOutputStream dos = new DataOutputStream(out);

            //핸들러에게 모든 것을 위임하는 것이 나을 것 같은데. (그 곳에서 스트림도 관리하게..)
           if (resource.startsWith("/user/create")) {
                signUpHandler.signUp(httpRequest.getBody());
                response302(dos, "/index.html");
                return;
            } else if (httpRequest.getMethod().equals(HttpMethod.GET) &&
                       httpRequest.getResource().contains("/css") || resource.contains("/fonts") || resource.contains("/images")
                       || resource.contains("/js") || resource.contains("/qna") || resource.contains("/user")
                       || resource.contains(".html") || resource.contains(".css") || resource.contains(".png") ||
                       resource.contains(".ico") || resource.contains(".js")) {
                body = resourceHandler.getResourceBytes(resource);
            } else {
                //사실상 exception 던져야하지 않을까 404
                body = "Hello World!!".getBytes();
            }

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {

        }
    }

    // RequestReader와 같은 전담 클래스를 만드는게 어떨까?
    // 적절한 요청을 적절한 핸들러에게 뿌려주는 역할을 하면 어떨까..
    private HttpRequest readRequest(BufferedReader br) throws HttpRequestParsingException {
        log.debug("\n\n-----------[read Request]");

        HttpMethod method = null;
        String resource = null;
        String scheme = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        String body = null;

        String line = null;
        boolean firstLine = true;

        try {
            while ((line = br.readLine()) != null) {
                log.debug(line);
                if(firstLine) {
                    String[] temp = line.split(" ");
                    method = HttpMethod.valueOf(temp[0]);
                    resource = temp[1];
                    scheme = temp[2];

                    if(method.equals(HttpMethod.GET) && resource.equals("/"))
                        resource = "/index.html";

                    firstLine = false;
                } else {
                    if(Strings.isNullOrEmpty(line)) {
                        break;
                    } else {
                        Pair pair = HttpRequestUtils.parseHeader(line);
                        headers.put(pair.getKey(), pair.getValue());
                    }
                }
            }

            if (method.equals(HttpMethod.POST)) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                body = IOUtils.readData(br, contentLength);
            }

            return new HttpRequest(method, resource, scheme, headers, body);
        } catch (Exception e) {
            log.error("readRequest Error: {}", e.getMessage());
            throw new HttpRequestParsingException(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302(DataOutputStream dos, String redirectLocation) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found : Moved Temporarily \r\n");
            dos.writeBytes("Location: " + redirectLocation + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

class HttpRequestParsingException extends Exception {
    public HttpRequestParsingException(String message) {
        super(message);
    }
}


