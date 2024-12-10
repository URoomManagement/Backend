package com.example.myproject.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    // GET: Fetch all rooms
    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // GET: Fetch a room by ID
    @GetMapping("/{id}")
    public Optional<Room> getRoomById(@PathVariable Long id) {
        return roomRepository.findById(id);
    }

    // POST: Create a new room
    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    // PUT: Update an existing room
    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        return roomRepository.findById(id)
                .map(room -> {
                    room.setName(updatedRoom.getName());
                    room.setLocation(updatedRoom.getLocation());
                    room.setInfo(updatedRoom.getInfo());
                    return roomRepository.save(room);
                })
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    // DELETE: Delete a user by ID
    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomRepository.deleteById(id);
    }
}
