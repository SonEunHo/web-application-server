package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpResponseUtils;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class AbstracrtHandler implements HttpHandler {
    Logger log = LoggerFactory.getLogger(AbstracrtHandler.class);

    @Override
    public HttpResponse service(HttpRequest httpRequest) {
        try {
            return HttpMethod.GET.equals(httpRequest.getMethod()) ?
                   doGet(httpRequest) : doPost(httpRequest);
        } catch (InvalidUrlException e) {
            log.error("invalid url exception occured = {}", httpRequest);
            return HttpResponseUtils.make_404_response();
        }
    }

    public abstract HttpResponse doPost(HttpRequest httpRequest);
    public abstract HttpResponse doGet(HttpRequest httpRequest);
}
