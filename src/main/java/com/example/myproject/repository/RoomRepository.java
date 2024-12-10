package com.example.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.myproject.Entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{
    
}
