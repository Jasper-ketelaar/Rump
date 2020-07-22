package dev.yasper.rump.exception;

public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public void onHttpException(HttpStatusCodeException e) {
        throw e;
    }

}
