package com.example.myproject.controller;

import com.example.myproject.Entity.Reservation;
import com.example.myproject.Entity.Room;
import com.example.myproject.Entity.User;
import com.example.myproject.repository.ReservationRepository;
import com.example.myproject.repository.RoomRepository;
import com.example.myproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    // GET: Fetch all reservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // GET: Fetch a reservation by ID
    @GetMapping("/{id}")
    public Optional<Reservation> getUserById(@PathVariable Long id) {
        return reservationRepository.findById(id);
    }

    // POST: Create a new Reservation
    @PostMapping
    public Reservation createReservation(@RequestParam Long roomId, @RequestParam Long userId, @RequestBody Reservation reservationDetails) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setPurpose(reservationDetails.getPurpose());
        reservation.setStartedAt(reservationDetails.getStartedAt());
        reservation.setEndedAt(reservationDetails.getEndedAt());

        return reservationRepository.save(reservation);
    }

    // PUT: Update an existing Reservation
    @PutMapping("/{id}")
    public Reservation updateUser(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setPurpose(updatedReservation.getPurpose());
                    reservation.setStartedAt(updatedReservation.getStartedAt());
                    reservation.setEndedAt(updatedReservation.getEndedAt());
                    reservation.setUser(updatedReservation.getUser());
                    reservation.setRoom(updatedReservation.getRoom());
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // DELETE: Delete a reservation by ID
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationRepository.deleteById(id);
    }
}
