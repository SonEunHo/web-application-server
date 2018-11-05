package webserver;

import org.junit.Test;

import webserver.WebServer.Phase;

public class WebServerTest {
    @Test
    public void phase() {
        Phase p = Phase.PRODUCTION;
        assert (p.equals(Phase.valueOf("production".toUpperCase())));
    }
}
