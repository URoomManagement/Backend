package com.example.myproject.Reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationService reservationService;
    

    // GET: Fetch all reservations
    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // GET: Fetch a reservation by ID
    @GetMapping("/{id}")
    public Optional<ReservationDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    // POST: Create a new Reservation
    @PostMapping
    public ReservationDTO createReservation(@RequestParam Long roomId, @RequestParam Long userId, @RequestBody Reservation reservationDetails) {
        return reservationService.createReservation(roomId, userId, reservationDetails);
    }

    // PUT: Update an existing Reservation
    @PutMapping("/{id}")
    public ReservationDTO updateReservation(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        return reservationService.updateReservation(id, updatedReservation);
    }

    // DELETE: Delete a reservation by ID
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationRepository.deleteById(id);
    }

    @GetMapping("/check")
    public ReservationDTO getReservationByDateAndRoom(@RequestParam Long roomId,
                                            @RequestParam LocalDateTime date) {
        return reservationService.getReservationsByRoomAndDate(roomId, date);
    }
}
