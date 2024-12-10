package com.example.myproject.Room;

import com.example.myproject.Enums.Location;

public record RoomDTO(
    Long id,
    String info,
    String name,
    Location location
) {}
