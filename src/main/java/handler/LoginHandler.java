package handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import service.UserService;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatusCode;

public class LoginHandler extends AbstracrtHandler {
    private Logger log = LoggerFactory.getLogger(LoginHandler.class);
    private UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        throw new InvalidUrlException();
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> param = HttpRequestUtils.parseQueryString(httpRequest.getBody());
        log.debug("login request {}", param); //password는 필터링 해야할텐데

        User user = userService.login(param.get("userId"), param.get("password"));

        Map<String, String> headers = new HashMap<>();
        if(user != null) {
            headers.put("Location", "/index.html");
            headers.put("Set-Cookie", "logined=true");
        } else {
            headers.put("Location", "/user/login_failed.html");
        }

        httpResponse.setStatusCode(HttpStatusCode.REDIRECT);
        httpResponse.setHeaders(headers);
        httpResponse.sendResponse();
    }
}
