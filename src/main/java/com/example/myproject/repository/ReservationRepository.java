package com.example.myproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.myproject.Entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    @Query("SELECT r FROM Reservation as r WHERE r.room.id = :roomId AND :date BETWEEN r.startedAt AND r.endedAt")
    List<Reservation> findByRoomAndDate(@Param("roomId") Long roomId, @Param("date") LocalDateTime date);   
}
