package handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.UserServiceImpl;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static HttpHandler resourceHandler;
    private static HttpHandler userHandler;
    private Socket connection;
    private static Map<String, HttpHandler> handlerMap;

    static {
        initHandlerMap();
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());
        HttpResponse response = null;

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            log.debug("http request: {}", httpRequest);

            delegateRequestToProperHandler(httpRequest, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void delegateRequestToProperHandler(HttpRequest httpRequest, OutputStream dos) throws IOException{
        String resource = httpRequest.getResource();
        if (handlerMap.containsKey(resource)) {
            handlerMap.get(resource).service(httpRequest, new HttpResponse(dos));
        } else {
            resourceHandler.service(httpRequest, new HttpResponse(dos));
        }
    }

    public static void initHandlerMap() {
        userHandler = new UserHandler(UserServiceImpl.getService());
        resourceHandler = new ResourceHandler();

        handlerMap = new HashMap<>();
        handlerMap.put("/user/create", userHandler);
        handlerMap.put("/user/login", userHandler);
        handlerMap.put("/user/list", userHandler);
    }
}

class HttpRequestParsingException extends Exception {
    public HttpRequestParsingException(String message) {
        super(message);
    }
}


