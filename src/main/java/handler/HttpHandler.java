package handler;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface HttpHandler {
    HttpResponse service(HttpRequest httpRequest);
}

class InvalidUrlException extends RuntimeException {

}