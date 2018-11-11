package handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import service.UserService;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatusCode;

public class UserHandler extends AbstracrtHandler {
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);
    //이거 나중에 통일되도록 뽑자
    private static final String WEB_RESOURCE_ROOT = "/Users/kakao/workspace/web-application-server/webapp";
    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpResponse doPost(HttpRequest httpRequest) {
        HttpResponse response = null;
        String resource = httpRequest.getResource();

        if("/user/create".equals(resource)) {
            response = signUp(httpRequest.getBody());
        } else if ("/user/login".equals(resource)) {
            response = login(httpRequest.getBody());
        } else {
            throw new InvalidUrlException();
        }

        return response;
    }

    @Override
    public HttpResponse doGet(HttpRequest httpRequest) {
        HttpResponse response;
        String resource = httpRequest.getResource();

        if ("/user/list".equals(resource)) {
            response = getUserList(httpRequest);
        } else {
            throw new InvalidUrlException();
        }

        return response;
    }

    private HttpResponse signUp(String query) {
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

    private HttpResponse login(String query) {
        log.debug("login request {}", query); //password는 필터링 해야할텐데
        Map<String, String> param = HttpRequestUtils.parseQueryString(query);
        User user = userService.login(param.get("userId"), param.get("password"));

        Map<String, String> headers = new HashMap<>();
        if(user != null) {
            headers.put("Location", "/index.html");
            headers.put("Set-Cookie", "logined=true");
        } else {
            headers.put("Location", "/user/login_failed.html");
        }

        return new HttpResponse(HttpStatusCode.REDIRECT, headers,null);
    }

    private HttpResponse getUserList(HttpRequest httpRequest) {
        boolean hasLoginCookie = false;
        Map<String, String> cookies = HttpRequestUtils.parseCookies(httpRequest.getHeaders().get("Cookie"));
        if(cookies.get("logined") != null && cookies.get("logined").equals("true")) {
            hasLoginCookie = true;
        }

        Map<String, String> headers = new HashMap<>();
        if (hasLoginCookie) {
            List<User> userList = userService.getUserList();
            log.debug("get user List: {}", userList);
            headers.put("Content-Type", "text/html;charset=utf-8");
            return new HttpResponse(HttpStatusCode.OK, headers, makeUserListHtml(userList));
        } else {
            headers.put("Location", "/user/login.html");
            return new HttpResponse(HttpStatusCode.REDIRECT, headers, null);
        }
    }

    private byte[] makeUserListHtml(final List<User> userList) {
        String line = "";
        StringBuilder body = new StringBuilder();
        File f = new File(WEB_RESOURCE_ROOT+"/user/list.html");
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {

            while((line = br.readLine()) != null) {
                body.append(line);
                if(line.contains("<tbody>"))
                    body.append(userListFormat(userList));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return body.toString().getBytes();
    }

    private String userListFormat(final List<User> userList) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int[] index = {0};

        userList.stream().forEach(u-> {
            stringBuilder.append(userRender(u, ++index[0]));
        });
        return stringBuilder.toString();
    }

    private String userRender(final User user, final int index) {
        return "<tr>\n" +
               "                    <th scope=\"row\">"+index+"</th> <td>"+user.getUserId()+"</td> <td>"+user.getName()+"</td> <td>"+user.getEmail() +
               "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
               "                </tr>\n";
    }
}
