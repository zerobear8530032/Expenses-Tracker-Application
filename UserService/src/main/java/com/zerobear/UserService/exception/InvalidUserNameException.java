package com.zerobear.UserService.exception;

public class InvalidUserNameException extends ValidationException{
    public InvalidUserNameException() {
    }

    public InvalidUserNameException(String message) {
        super(message);
    }

    public InvalidUserNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserNameException(Throwable cause) {
        super(cause);
    }

    public InvalidUserNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
