package webserver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import service.UserService;
import util.HttpRequestUtils;

public class UserHandler {
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);
    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public HttpResponse signUp(String query) {
        log.debug("sign up request : {}", query);
        Map<String, String> param = HttpRequestUtils.parseQueryString(query);
        User signUpUser = new User(param.get("userId"),
                                   param.get("password"),
                                   param.get("name"),
                                   param.get("email"));
        userService.signUp(signUpUser);

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        return new HttpResponse(HttpStatusCode.REDIRECT, headers, null);
    }

    public HttpResponse login(String query) {
        log.debug("login request {}", query); //password는 필터링 해야할텐데
        Map<String, String> param = HttpRequestUtils.parseQueryString(query);
        User user = userService.login(param.get("userId"), param.get("password"));

        Map<String, String> headers = new HashMap<>();
        if(user != null) {
            headers.put("Location", "/index.html");
            headers.put("Set-Cookie", "logined=true");
            return new HttpResponse(HttpStatusCode.REDIRECT, headers,null);
        } else {
            headers.put("Location", "/user/login_failed.html");
            return new HttpResponse(HttpStatusCode.REDIRECT, headers,null);
        }
    }
}
