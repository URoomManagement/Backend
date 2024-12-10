package com.example.myproject.Room;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    
    public List<RoomDTO> getAllRooms(){
        return roomRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Optional<RoomDTO> getRoomById(Long id){
        return roomRepository.findById(id)
                .map(this::mapToDTO);
    }

    public RoomDTO createRoom(Room room){
        return mapToDTO(roomRepository.save(room));
    }

    public RoomDTO updateRoom(Long id, Room updatedRoom){
        return roomRepository.findById(id)
                .map(room -> {
                    room.setName(updatedRoom.getName());
                    room.setLocation(updatedRoom.getLocation());
                    room.setInfo(updatedRoom.getInfo());
                    return mapToDTO(roomRepository.save(room));
                })
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    private RoomDTO mapToDTO(Room room){
        return new RoomDTO(
            room.getId(),
            room.getInfo(),
            room.getName(), 
            room.getLocation()
        );
    }
}
