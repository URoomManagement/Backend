package com.example.myproject.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // Set the appropriate status code
    public Map<String, String> handleDatabaseException(DatabaseException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Database error");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    public Map<String, String> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "User not found");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
    public Map<String, String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid credentials");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(RoomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    public Map<String, String> handleRoomNotFoundException(RoomNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Room not found");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    public Map<String, String> handleReservationNotFoundException(ReservationNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Reservation not found");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(RoomAlreadyReserved.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    public Map<String, String> handleRoomAlreadyReservedException(RoomAlreadyReserved ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Room Already Reserved");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }
}
