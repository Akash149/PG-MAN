package com.pgman.service.pg;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgman.entities.pg.Room;

@Service
public interface RoomService {

    // 1. Create/Add a Room
    public Room addRoom(Room room);

    // 2. Get All Room
    public List<Room> getAllRoom();

    // 3. Get a single Room
    public Room getARoom(int id);

    // 4. Update a Room 
    public Room updateRoom(int id, Room room);

    // 5. Delete a room
    public void deleteRoom(int id);
    
}
