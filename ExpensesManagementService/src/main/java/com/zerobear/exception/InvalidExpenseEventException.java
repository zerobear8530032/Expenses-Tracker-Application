package com.zerobear.exception;

public class InvalidExpenseEventException extends RuntimeException{
    public InvalidExpenseEventException() {
    }

    public InvalidExpenseEventException(String message) {
        super(message);
    }

    public InvalidExpenseEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidExpenseEventException(Throwable cause) {
        super(cause);
    }

    public InvalidExpenseEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
