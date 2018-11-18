package webserver;

import java.util.UUID;

import org.junit.Test;

public class UuidTest {
    @Test
    public void uuidCreation() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString());
    }
}
