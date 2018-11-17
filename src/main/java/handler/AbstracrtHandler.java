package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatusCode;

public abstract class AbstracrtHandler implements HttpHandler {
    Logger log = LoggerFactory.getLogger(AbstracrtHandler.class);

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            if(HttpMethod.GET.equals(httpRequest.getMethod()))
                doGet(httpRequest, httpResponse);
            else
                doPost(httpRequest, httpResponse);
        } catch (InvalidUrlException e) {
            log.error("invalid url exception occured = {}", httpRequest);
//            HttpResponseUtils.make_404_response();
            httpResponse.sendErrorResponse(HttpStatusCode.NOT_FOUND);
        }
    }

    public abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse);
    public abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse);
}
