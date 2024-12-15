package com.example.myproject.Reservation;

import com.example.myproject.Room.RoomRepository;
import com.example.myproject.User.UserRepository;
import com.example.myproject.Exceptions.ReservationNotFoundException;
import com.example.myproject.Exceptions.RoomAlreadyReserved;
import com.example.myproject.Exceptions.RoomNotFoundException;
import com.example.myproject.Exceptions.UserNotFoundException;
import com.example.myproject.Room.Room;
import com.example.myproject.User.User;

import java.time.LocalDateTime;
import java.util.List;

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

    public ReservationDTO getReservationById(Long id){
        return reservationRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
    }

    public ReservationsPerRoomDTO getReservationsByRoomId(Long roomId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        List<Reservation> reservations = reservationRepository.findByRoomId(roomId);

        List<ReservationPerRoomDTO> reservationDTOs = reservations.stream()
                .map(reservation -> new ReservationPerRoomDTO(
                    reservation.getId(),
                    reservation.getPurpose(),
                    reservation.getStartedAt(),
                    reservation.getEndedAt(),
                    reservation.getUser().getName()
                ))
                .toList();

        return new ReservationsPerRoomDTO(
            room.getLocation(),
            room.getName(),
            room.getInfo(),
            reservationDTOs
        );
    }

    public List<ReservationDTO> getReservationsByUserId(Long userId){
        return reservationRepository.findByUserIdOrderByStartedAtDesc(userId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ReservationDTO createReservation(ReservationRequest reservationDetails){
        Room room = roomRepository.findById(reservationDetails.getRoomId())
            .orElseThrow(() -> new RoomNotFoundException("Room not found"));
        User user = userRepository.findById(reservationDetails.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check whether there is already overlapping reservation
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                reservationDetails.getRoomId(), reservationDetails.getStartedAt(), reservationDetails.getEndedAt());

        if (!overlappingReservations.isEmpty()) {
            throw new RoomAlreadyReserved("The room is already reserved during the specified time.");
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
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
    }

    public ReservationDTO getReservationsByRoomAndDate(Long roomId, LocalDateTime date) {
        List<Reservation> reservations = reservationRepository.findByRoomAndDate(roomId, date);
        if(reservations.isEmpty()) return null;
        return reservations.stream()
                .map(this::mapToDTO)
                .toList().get(0);
    }

    public void deleteReservation(Long id){
        if(!reservationRepository.existsById(id)){
            throw new ReservationNotFoundException("Reservation not Found");
        }
        reservationRepository.deleteById(id);
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

