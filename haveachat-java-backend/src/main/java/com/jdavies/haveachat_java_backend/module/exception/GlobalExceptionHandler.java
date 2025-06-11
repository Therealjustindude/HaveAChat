package com.jdavies.haveachat_java_backend.module.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        logger.error("Error occurred: {}", ex.getMessage());

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("errorType", ex.getErrorType().name());
        errorBody.put("message", ex.getCustomMessage());

        HttpStatus status = mapErrorTypeToStatus(ex.getErrorType());

        return new ResponseEntity<>(errorBody, status);
    }

    private HttpStatus mapErrorTypeToStatus(ErrorType errorType) {
        switch (errorType) {
            case BAD_REQUEST:
                return HttpStatus.BAD_REQUEST;
            case CONFLICT:
                return HttpStatus.CONFLICT;
            case UNAUTHORIZED:
                return HttpStatus.UNAUTHORIZED;
            case FORBIDDEN:
                return HttpStatus.FORBIDDEN;
            case NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case INTERNAL_SERVER_ERROR:
                return HttpStatus.INTERNAL_SERVER_ERROR;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}