package webserver;

public interface HttpHandler {
    HttpResponse service(HttpRequest httpRequest);
}
