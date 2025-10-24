package com.dexwin.notesapp.errors;

import com.dexwin.notesapp.models.ApiError;
import com.dexwin.notesapp.models.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponseDTO> handleNoSuchElementException(final NoSuchElementException e) {
        final ApiError error = new ApiError("Not Found", e.getMessage());
        final ApiResponseDTO apiResponseDTO = new ApiResponseDTO(false, null, error);
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO> handleIllegalArgumentException(final IllegalArgumentException e) {
        final ApiError error = new ApiError("Bad Request", e.getMessage());
        final ApiResponseDTO apiResponseDTO = new ApiResponseDTO(false, null, error);
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleAllException(final Exception e) {
        final ApiError error = new ApiError("Internal Server Error", e.getMessage());
        final ApiResponseDTO apiResponseDTO = new ApiResponseDTO(false, null, error);
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String defaultMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation failed");

        Map<String, String> response = new HashMap<>();
        response.put("message", defaultMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
