package com.zerobear.ExpensesManagementApplication.exception;

public class RefrehTokenNotFoundException extends  NotFoundException{
    public RefrehTokenNotFoundException(String message) {
        super(message);
    }

    public RefrehTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefrehTokenNotFoundException(Throwable cause) {
        super(cause);
    }

    public RefrehTokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RefrehTokenNotFoundException() {
    }
}
