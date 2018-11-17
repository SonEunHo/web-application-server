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

public class SignupHandler extends AbstracrtHandler {
    private Logger log = LoggerFactory.getLogger(SignupHandler.class);
    private UserService userService;

    public SignupHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> param = HttpRequestUtils.parseQueryString(httpRequest.getBody());
        log.debug("sign up request : {}", param);

        User signUpUser = new User(param.get("userId"),
                                   param.get("password"),
                                   param.get("name"),
                                   param.get("email"));
        userService.signUp(signUpUser);

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        httpResponse.setStatusCode(HttpStatusCode.REDIRECT);
        httpResponse.setHeaders(headers);
        httpResponse.sendResponse();
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        throw new InvalidUrlException();
    }
}
