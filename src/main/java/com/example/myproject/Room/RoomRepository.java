package com.example.myproject.Room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myproject.Enums.Location;

public interface RoomRepository extends JpaRepository<Room, Long>{
    List<Room> findByLocationOrderByNameAsc(Location location);
}
