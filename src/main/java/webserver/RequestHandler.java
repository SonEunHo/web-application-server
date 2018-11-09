package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import service.UserServiceImpl;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private ResourceHandler resourceHandler;
    private UserHandler userHandler;
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        resourceHandler = new ResourceHandler();
        userHandler = new UserHandler(UserServiceImpl.getService());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = readRequest(new BufferedReader(new InputStreamReader(in)));
            log.debug("http request: {}", httpRequest);

            byte[] body;
            String resource = httpRequest.getResource();
            HttpResponse response = null;
            //핸들러에게 모든 것을 위임하는 것이 나을 것 같은데. (그 곳에서 스트림도 관리하게..)
            if (resource.equals("/user/create")) {
                response = userHandler.signUp(httpRequest.getBody());
            } else if(resource.equals("/user/login")) {
                response = userHandler.login(httpRequest.getBody());
            } else if(resource.equals("/user/list")) {
                response = userHandler.getUserList(httpRequest);
            } else if (httpRequest.getMethod().equals(HttpMethod.GET) &&
                       httpRequest.getResource().contains("/css") || resource.contains("/fonts") || resource.contains("/images")
                       || resource.contains("/js") || resource.contains("/qna") || resource.contains("/user")
                       || resource.contains(".html") || resource.contains(".css") || resource.contains(".png") ||
                       resource.contains(".ico") || resource.contains(".js")) {
                response = resourceHandler.getResource(resource);
            } else {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "text/html;charset=utf-8");
                response = new HttpResponse(HttpStatusCode.NOT_FOUND, headers, null);
            }

            sendResponse(new DataOutputStream(out), response);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
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
            throw new HttpRequestParsingException(e.getMessage());
        }

        return new HttpRequest(method, resource, scheme, headers, body);
    }

    private void sendResponse(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes("HTTP/1.1 "+response.getStatusCode().getStatusCode()+" "+response.getStatusCode().getMessage()+"\r\n");
            response.getHeaders().forEach((k,v) -> {
                try {
                    dos.writeBytes(k + ": " + v+"\r\n");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });
            if(response.getBody() != null) {
                dos.writeBytes("Content-Length: "+response.getBody().length+"\r\n");
                dos.writeBytes("\r\n");
                dos.write(response.getBody(), 0, response.getBody().length);
            } else {
                dos.writeBytes("\r\n");
            }
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


