package com.example.myproject.Reservation;

import java.time.LocalDateTime;
import com.example.myproject.Enums.Location;

public record ReservationDTO(
    Long id,
    String purpose,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    Location roomLocation,
    String roomName,
    String roomInfo,
    String userName
) {}
