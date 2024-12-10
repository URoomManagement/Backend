package com.example.myproject.Reservation;

import com.example.myproject.Room.RoomRepository;
import com.example.myproject.User.UserRepository;
import com.example.myproject.Room.Room;
import com.example.myproject.User.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    public List<ReservationDTO> getAllReservations(){
        return reservationRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Optional<ReservationDTO> getReservationById(Long id){
        return reservationRepository.findById(id)
                .map(this::mapToDTO);
    }

    public ReservationDTO createReservation(Long roomId, Long userId, Reservation reservationDetails){
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Room not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check whether there is already overlapping reservation
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                roomId, reservationDetails.getStartedAt(), reservationDetails.getEndedAt());

        if (!overlappingReservations.isEmpty()) {
            throw new RuntimeException("The room is already reserved during the specified time.");
        }

        // Create a new one if not overlapping
        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setPurpose(reservationDetails.getPurpose());
        reservation.setStartedAt(reservationDetails.getStartedAt());
        reservation.setEndedAt(reservationDetails.getEndedAt());

        return mapToDTO(reservationRepository.save(reservation));
    }

    public ReservationDTO updateReservation(Long id, Reservation updatedReservation){
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setPurpose(updatedReservation.getPurpose());
                    reservation.setStartedAt(updatedReservation.getStartedAt());
                    reservation.setEndedAt(updatedReservation.getEndedAt());
                    reservation.setUser(updatedReservation.getUser());
                    reservation.setRoom(updatedReservation.getRoom());
                    return mapToDTO(reservationRepository.save(reservation));
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

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

