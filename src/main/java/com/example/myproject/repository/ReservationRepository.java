package com.example.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.myproject.Entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    
}
