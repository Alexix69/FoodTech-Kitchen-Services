package com.foodtech.kitchen.infrastructure.rest.exception;

import com.foodtech.kitchen.application.exceptions.AccessDeniedException;
import com.foodtech.kitchen.application.exceptions.InvalidTaskTransitionException;
import com.foodtech.kitchen.application.exceptions.OrderNotFoundException;
import com.foodtech.kitchen.application.exceptions.RoleAlreadyAssignedException;
import com.foodtech.kitchen.application.exceptions.RoleNotAssignedException;
import com.foodtech.kitchen.application.exceptions.TaskNotFoundException;
import com.foodtech.kitchen.application.exceptions.DuplicateEmailException;
import com.foodtech.kitchen.application.exceptions.DuplicateUsernameException;
import com.foodtech.kitchen.infrastructure.rest.dto.ErrorResponse;
import com.foodtech.kitchen.infrastructure.rest.dto.RoleNotAssignedResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Order not found",
            HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Task not found",
            HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Duplicate email",
            HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Duplicate username",
            HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Validation failed",
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Invalid state transition",
            HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected one of: BAR, HOT_KITCHEN, COLD_KITCHEN", 
            ex.getValue(), ex.getName());
        ErrorResponse error = new ErrorResponse(
            message,
            "Invalid parameter type",
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .findFirst()
            .orElse("Validation failed");
        ErrorResponse error = new ErrorResponse(
            message,
            "Validation failed",
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse error = new ErrorResponse(
            "Malformed JSON request",
            "Validation failed",
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            "Internal server error",
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(RoleNotAssignedException.class)
    public ResponseEntity<RoleNotAssignedResponse> handleRoleNotAssignedException(RoleNotAssignedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new RoleNotAssignedResponse("ROLE_NOT_ASSIGNED", ex.getUserId()));
    }

    @ExceptionHandler(RoleAlreadyAssignedException.class)
    public ResponseEntity<ErrorResponse> handleRoleAlreadyAssignedException(RoleAlreadyAssignedException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Role already assigned",
            HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Access denied",
            HttpStatus.FORBIDDEN.value()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(InvalidTaskTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTaskTransitionException(InvalidTaskTransitionException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Invalid task transition",
            HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
