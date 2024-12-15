package com.example.myproject.Room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.myproject.Enums.Location;
import com.example.myproject.Exceptions.DatabaseException;
import com.example.myproject.Exceptions.RoomNotFoundException;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    
    public List<RoomDTO> getAllRooms(){
        return roomRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<RoomDTO> getAllRoomsPerLocation(Location locationName){
        return roomRepository.findByLocationOrderByNameAsc(locationName).stream()
                .map(this::mapToDTO)
                .toList();
    }

    public RoomDTO getRoomById(Long id){
        return roomRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));
    }

    public RoomDTO createRoom(Room room){
        try{
            return mapToDTO(roomRepository.save(room));
        } catch(DataAccessException e){
            throw new DatabaseException("Failed to save room to the database: " + e.getMessage());
        }
    }

    public RoomDTO updateRoom(Long id, Room updatedRoom){
        return roomRepository.findById(id)
                .map(room -> {
                    room.setName(updatedRoom.getName());
                    room.setLocation(updatedRoom.getLocation());
                    room.setInfo(updatedRoom.getInfo());
                    return mapToDTO(roomRepository.save(room));
                })
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));
    }

    public void deleteRoom(Long id){
        if(!roomRepository.existsById(id)){
            throw new RoomNotFoundException("Room not Found");
        }
        roomRepository.deleteById(id);
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
