package org.lean.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.lean.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    private static Map<String, Object> createErrorResponse(
        Throwable e, HttpServletRequest request, HttpStatus status)
    {
        return createErrorResponse(request, status, e.getMessage() == null ? "null" : e.getMessage());
    }

    private static Map<String, Object> createErrorResponse(
        HttpServletRequest request, HttpStatus status, String message)
    {
        return Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "status", status.value(),
            "error", status.getReasonPhrase(),
            "message", message,
            "path", "" + request.getContextPath() + request.getServletPath()
        );
    }

    private static String buildRejectedFieldsMessage(List<FieldError> fieldErrors) {
        return "rejectedFields: "
               + fieldErrors.stream()
                            .map(error -> String.format("'%s' %s", error.getField(), error.getDefaultMessage()))
                            .collect(Collectors.toList());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
        ResourceNotFoundException ex, HttpServletRequest request)
    {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(createErrorResponse(ex, request, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
        ResourceNotFoundException ex, HttpServletRequest request)
    {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body(createErrorResponse(ex, request, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex, HttpServletRequest request)
    {
        String message = buildRejectedFieldsMessage(ex.getBindingResult().getFieldErrors());

        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(createErrorResponse(request, HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
        HttpMessageNotReadableException ex, HttpServletRequest request)
    {
        log.error(ex.getMessage(), ex);
        Throwable cause = ex.getRootCause();
        if (cause instanceof InvalidFormatException) {
            return ResponseEntity
                       .status(HttpStatus.BAD_REQUEST)
                       .body(createErrorResponse(request, HttpStatus.BAD_REQUEST, cause.getLocalizedMessage()));
        }
        return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body(createErrorResponse(ex, request, HttpStatus.BAD_REQUEST));
    }
}
