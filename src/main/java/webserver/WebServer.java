package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8268;
    private static Phase phase;

    public static void main(String args[]) throws Exception {
        int port = 0;
        phase = Phase.DEVELOP;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
            if (args.length == 2)

                phase = Phase.valueOf(args[1].toUpperCase());
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);
            log.info("Web Application Server phase: {}", phase);


            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection);
                requestHandler.start();
            }
        }
    }

    public static Phase getPhase() {
        return phase;
    }

    enum Phase {
        DEVELOP("develop"), PRODUCTION("production");
        private final String phase;

        Phase(String phase) {
            this.phase = phase;
        }

        public String getPhase() {
            return phase;
        }

        @Override
        public String toString() {
            return getPhase();
        }
    }
}
