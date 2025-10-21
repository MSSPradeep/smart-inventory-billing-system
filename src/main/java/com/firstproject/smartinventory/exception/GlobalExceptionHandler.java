package com.firstproject.smartinventory.exception;


import com.firstproject.smartinventory.exception.auth.ForbiddenActionException;
import com.firstproject.smartinventory.exception.auth.UnauthorizedAccessException;
import com.firstproject.smartinventory.exception.badRequest.DuplicateEntryException;
import com.firstproject.smartinventory.exception.badRequest.InsufficientStockException;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.exception.notFound.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//  Handles the 404 NOT FOUND Exception
    @ExceptionHandler({ProductNotFoundException.class,
                      SaleItemNotFoundException.class,
                      SaleNotFoundException.class,
                      StoreNotFoundException.class,
                      UserNotFoundException.class,
                      CategoriesNotFoundException.class})
    public ResponseEntity<String> handleNotFound(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

//  Handles the 400 BAD REQUEST Exception
    @ExceptionHandler({
            DuplicateEntryException.class,
            InsufficientStockException.class,
            InvalidInputException.class
    })
    public ResponseEntity<String> handleBadRequest(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // Loop through all the field errors
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField(); // The name of the field that failed
            String errorMessage = error.getDefaultMessage(); // The message from the annotation
            errors.put(fieldName, errorMessage);
        });

        // Return the map of errors with a 400 status
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

//  Handles the 403 FORBIDDEN Exception
    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<String> handleForbidden(RuntimeException ex ){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

// Handles the 401 UNAUTHORIZED Exception
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String > handleUnauthorized(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());

    }

//  Handles the 500 INTERNAL SERVER ERROR
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<String> handleServerErrors(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
