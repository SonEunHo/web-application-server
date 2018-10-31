package webserver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import service.SignUpService;
import util.HttpRequestUtils;

public class SignUpHandler {
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);
    private SignUpService signUpService;

    public SignUpHandler(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    public HttpResponse signUp(String query) {
        log.debug("sign up request : {}", query);
        Map<String, String> param = HttpRequestUtils.parseQueryString(query);
        User signUpUser = new User(param.get("userId"),
                                   param.get("password"),
                                   param.get("name"),
                                   param.get("email"));

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        return new HttpResponse(HttpStatusCode.REDIRECT, headers, null);
    }
}
