package com.example.myproject.Reservation;

import java.util.List;

import com.example.myproject.Enums.Location;

public record ReservationsPerRoomDTO(
    Location roomLocation,
    String roomName,
    String roomInfo,
    List<ReservationPerRoomDTO> reservations
) {}
