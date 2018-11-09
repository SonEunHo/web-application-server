package webserver;

public enum HttpStatusCode {
    OK(200, "OK"),
    REDIRECT(302, "Found : Moved Temporarily"),
    CLIENT_ERROR(400, "error"),
    NOT_FOUND(404, "not found"),
    INTERNAL_ERROR(500, "server error");

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
