package com.example.myproject.Exceptions;

public class RoomAlreadyReserved extends RuntimeException {
    public RoomAlreadyReserved(String message) {
        super(message);
    }
}
