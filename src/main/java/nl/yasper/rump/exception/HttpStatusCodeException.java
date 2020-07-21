package nl.yasper.rump.exception;

import nl.yasper.rump.response.HttpResponse;

public class HttpStatusCodeException extends RuntimeException {

    private final HttpResponse<String> errorResponse;

    public HttpStatusCodeException(HttpResponse<String> errorResponse) {
        super(errorResponse.getResponseMessage());
        this.errorResponse = errorResponse;
    }

    public HttpResponse<String> getErrorResponse() {
        return errorResponse;
    }
}
