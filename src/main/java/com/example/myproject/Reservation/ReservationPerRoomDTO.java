package com.example.myproject.Reservation;

import java.time.LocalDateTime;

public record ReservationPerRoomDTO(
    Long id,
    String purpose,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    String userName
) {}
