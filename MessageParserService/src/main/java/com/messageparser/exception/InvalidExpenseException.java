package com.messageparser.exception;

public class InvalidExpenseException extends RuntimeException{
    public InvalidExpenseException() {
    }

    public InvalidExpenseException(String message) {
        super(message);
    }

    public InvalidExpenseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidExpenseException(Throwable cause) {
        super(cause);
    }

    public InvalidExpenseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
