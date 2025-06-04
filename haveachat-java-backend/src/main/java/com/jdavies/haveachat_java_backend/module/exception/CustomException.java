package com.jdavies.haveachat_java_backend.module.exception;

public class CustomException extends RuntimeException {
    private final ErrorType errorType;
    private final String customMessage;

    // Constructor to accept error type and a custom message
    public CustomException(ErrorType errorType, String message) {
        super(message); // This is the message that will be passed to the superclass (RuntimeException)
        this.errorType = errorType;
        this.customMessage = message;
    }

    // Constructor to accept only error type (default message)
    public CustomException(ErrorType errorType) {
        this(errorType, errorType.name() + " occurred.");
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    @Override
    public String getMessage() {
        return "Error Type: " + errorType.name() + " - " + customMessage;
    }
}