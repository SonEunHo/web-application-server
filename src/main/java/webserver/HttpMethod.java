package webserver;

public enum HttpMethod {
    GET("GET"), POST("POST"), DELETE("DELETE"), UPDATE("UPDATE");

    private String method;

    HttpMethod(String method) {
        this.method = method;
    }
}
