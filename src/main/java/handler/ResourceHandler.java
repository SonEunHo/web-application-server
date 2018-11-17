package handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatusCode;
import webserver.WebServer;
import webserver.WebServer.Phase;

public class ResourceHandler extends AbstracrtHandler {
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);
    private static final String DEVELOP_WEB_RESOURCE_ROOT = "/Users/kakao/workspace/web-application-server/webapp";
    private static final String PRODUCTION_WEB_RESOURCE_ROOT = "/home/deploy/www/web-application-server/webapp";

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        throw new InvalidUrlException();
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        String resource = httpRequest.getResource();

        if (httpRequest.getResource().contains("/css") || resource.contains("/fonts") || resource.contains(
                "/images")
            || resource.contains("/js") || resource.contains("/qna") || resource.contains("/user")
            || resource.contains(".html") || resource.contains(".css") || resource.contains(".png")
            || resource.contains(".ico") || resource.contains(".js")) {
            getResource(resource, httpResponse);
        } else {
            throw new InvalidUrlException();
        }

        httpResponse.sendResponse();
    }

    //나중에 익셉션 만들어서 던지는게 낫겠다.
    public void getResource(String resourcePath, HttpResponse httpResponse) {
        File file = new File(getWebResourceRoot() + resourcePath);

        Map<String, String> headers = new HashMap<>();
        if(resourcePath.contains(".css"))
            headers.put("Content-Type", "text/css");
        else
            headers.put("Content-Type", "text/html;charset=utf-8");

        try {
            httpResponse.setStatusCode(HttpStatusCode.OK);
            httpResponse.setHeaders(headers);
            httpResponse.setBody(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            httpResponse.setStatusCode(HttpStatusCode.INTERNAL_ERROR);
        }
    }

    public String getWebResourceRoot() {
        return WebServer.getPhase().equals(Phase.PRODUCTION) ? PRODUCTION_WEB_RESOURCE_ROOT : DEVELOP_WEB_RESOURCE_ROOT;
    }
}
