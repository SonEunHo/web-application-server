package webserver;

public enum HttpStatusCode {
    OK(200, "OK"), REDIRECT(302, "Found : Moved Temporarily"), CLIENT_ERROR(400, "error");

    private int statusCode;
    private String message;

    HttpStatusCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
