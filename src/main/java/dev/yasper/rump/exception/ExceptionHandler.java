package dev.yasper.rump.exception;

public interface ExceptionHandler {

    void onHttpException(HttpStatusCodeException e);

}
