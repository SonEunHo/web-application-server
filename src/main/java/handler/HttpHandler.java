package handler;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface HttpHandler {
    void service(HttpRequest httpRequest, HttpResponse httpResponse);
}

class InvalidUrlException extends RuntimeException {

}