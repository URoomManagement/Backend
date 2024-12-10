package com.example.myproject.Reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public ReservationDTO getReservationsByRoomAndDate(Long roomId, LocalDateTime date) {
        List<Reservation> reservations = reservationRepository.findByRoomAndDate(roomId, date);
        if(reservations.isEmpty()) return null;
        return reservations.stream()
                .map(this::mapToDTO)
                .toList().get(0);
    }

    private ReservationDTO mapToDTO(Reservation reservation) {
        return new ReservationDTO(
            reservation.getId(),
            reservation.getPurpose(),
            reservation.getStartedAt(),
            reservation.getEndedAt(),
            reservation.getRoom().getLocation(),
            reservation.getRoom().getName(),
            reservation.getRoom().getInfo(),
            reservation.getUser().getName()
        );
    }
}

