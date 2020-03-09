package io.home.webfluxapp.exception;

public class CustomException extends Throwable {

    private String message;

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomException(Throwable throwable) {
        this.message = throwable.getMessage();
    }
}
