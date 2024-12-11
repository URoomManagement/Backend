package com.example.myproject.Reservation;

import java.time.LocalDateTime;

public class ReservationRequest {
    private Long roomId;
    private Long userId;
    private String purpose;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public Long getUserId(){
        return userId;
    }
    
    public Long getRoomId(){
        return roomId;
    }

    public String getPurpose(){
        return purpose;
    }

    public LocalDateTime getStartedAt(){
        return startedAt;
    }
    public LocalDateTime getEndedAt(){
        return endedAt;
    }
}
