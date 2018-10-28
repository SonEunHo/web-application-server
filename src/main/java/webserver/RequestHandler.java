package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import service.SignUpServiceImpl;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private ResourceHandler resourceHandler;
    private SignUpHandler signUpHandler;
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        resourceHandler = new ResourceHandler();
        signUpHandler = new SignUpHandler(SignUpServiceImpl.getService());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            String query = readRequest(new BufferedReader(new InputStreamReader(in)));
            log.debug("query: {}", query);

            byte[] body;
            if (query.contains("/user/create")) {
                body = signUpHandler.signUp(query);
            } else if (query.contains("/css") || query.contains("/fonts") || query.contains("/images")
                       || query.contains("/js") || query.contains("/qna") || query.contains("/user")
                       || query.contains(".html") || query.contains(".css") || query.contains(".png") ||
                       query.contains(".ico") || query.contains(".js")) {
                body = resourceHandler.getResourceBytes(query);
            } else {
                body = "Hello World!!".getBytes();
            }

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    // RequestReader와 같은 전담 클래스를 만드는게 어떨까?
    private String readRequest(BufferedReader br) {
        String line = null;
        String query = null;
        log.debug("\n\n-----------[read Request]");
        boolean firstLine = true;
        try {
            while ((line = br.readLine()) != null) {
                log.debug(line);
                if(firstLine) {
                    if(line.contains("GET")) {
                        query = line.split(" ")[1];
                        if(query.equals("/")) query = "/index.html";
                    }
                    firstLine = false;
                } else {
                    if(Strings.isNullOrEmpty(line)) break;
                }
            }
        } catch (Exception e) {
            log.error("readRequest Error: {}", e.getMessage());
        }

        return query;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
