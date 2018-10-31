package webserver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceHandler {
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);
    private static final String WEB_RESOURCE_ROOT = "/Users/kakao/workspace/web-application-server/webapp";

    //나중에 익셉션 만들어서 던지는게 낫겠다.
    public HttpResponse getResource(String resourcePath) throws IOException {
        File file = new File(WEB_RESOURCE_ROOT + resourcePath);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html;charset=utf-8");

        return new HttpResponse(HttpStatusCode.OK, headers, Files.readAllBytes(file.toPath()));
    }

}
