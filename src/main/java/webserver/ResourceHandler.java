package webserver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpResponseUtils;
import webserver.WebServer.Phase;

public class ResourceHandler implements HttpHandler{
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);
    private static final String DEVELOP_WEB_RESOURCE_ROOT = "/Users/kakao/workspace/web-application-server/webapp";
    private static final String PRODUCTION_WEB_RESOURCE_ROOT = "/home/deploy/www/web-application-server/webapp";

    @Override
    public HttpResponse service(HttpRequest httpRequest) {
        String resource = httpRequest.getResource();
        HttpResponse response;
        if (httpRequest.getMethod().equals(HttpMethod.GET) &&
            httpRequest.getResource().contains("/css") || resource.contains("/fonts") || resource.contains("/images")
            || resource.contains("/js") || resource.contains("/qna") || resource.contains("/user")
            || resource.contains(".html") || resource.contains(".css") || resource.contains(".png")
            || resource.contains(".ico") || resource.contains(".js")) {
            response = getResource(resource);
        } else {
            response = HttpResponseUtils.make_404_response();
        }

        return response;
    }

    //나중에 익셉션 만들어서 던지는게 낫겠다.
    public HttpResponse getResource(String resourcePath) {
        File file = new File(getWebResourceRoot() + resourcePath);

        Map<String, String> headers = new HashMap<>();
        if(resourcePath.contains(".css"))
            headers.put("Content-Type", "text/css");
        else
            headers.put("Content-Type", "text/html;charset=utf-8");

        try {
            return new HttpResponse(HttpStatusCode.OK, headers, Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            return HttpResponseUtils.make_500_response();
        }
    }

    public String getWebResourceRoot() {
        return WebServer.getPhase().equals(Phase.PRODUCTION) ? PRODUCTION_WEB_RESOURCE_ROOT : DEVELOP_WEB_RESOURCE_ROOT;
    }
}
