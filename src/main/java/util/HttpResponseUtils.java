package util;

import java.util.HashMap;
import java.util.Map;

import webserver.HttpResponse;
import webserver.HttpStatusCode;

public class HttpResponseUtils {
    public static HttpResponse make_404_response() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html;charset=utf-8");
        return new HttpResponse(HttpStatusCode.NOT_FOUND, headers, null);
    }

    public static HttpResponse make_500_response() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html;charset=utf-8");
        return new HttpResponse(HttpStatusCode.INTERNAL_ERROR, headers, null);
    }
}
