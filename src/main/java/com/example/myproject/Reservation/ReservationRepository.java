package com.example.myproject.Reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND :startedAt < r.endedAt AND :endedAt > r.startedAt")
    List<Reservation> findOverlappingReservations(@Param("roomId") Long roomId,
                                              @Param("startedAt") LocalDateTime startedAt,
                                              @Param("endedAt") LocalDateTime endedAt);

    @Query("SELECT r FROM Reservation as r WHERE r.room.id = :roomId AND :date BETWEEN r.startedAt AND r.endedAt")
    List<Reservation> findByRoomAndDate(@Param("roomId") Long roomId, @Param("date") LocalDateTime date);   
    
    List<Reservation> findByRoomId(Long roomId);

}
