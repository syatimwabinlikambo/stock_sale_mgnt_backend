package com.syatimwaraph.stock_sale_mgnt_v1.exception;

import com.syatimwaraph.stock_sale_mgnt_v1.errorhandling.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * =====================================================
     * VALIDATION ERRORS
     * =====================================================
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error :
                exception.getBindingResult().getFieldErrors()) {

            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }


    /*
     * =====================================================
     * DUPLICATE EMAIL
     * =====================================================
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExists(
            EmailAlreadyExistsException exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(apiError);
    }


    /*
     * =====================================================
     * INVALID CREDENTIALS
     * =====================================================
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(
            InvalidCredentialsException exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(apiError);
    }


    /*
     * =====================================================
     * DISABLED ACCOUNT
     * =====================================================
     */
    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<ApiError> handleDisabledAccount(
            AccountDisabledException exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(apiError);
    }


    /*
     * =====================================================
     * SPRING SECURITY BAD CREDENTIALS
     * =====================================================
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(
            BadCredentialsException exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid email or password"
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(apiError);
    }


    /*
     * =====================================================
     * GENERIC EXCEPTION
     * =====================================================
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception exception
    ) {

        exception.printStackTrace();

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError);
    }
}