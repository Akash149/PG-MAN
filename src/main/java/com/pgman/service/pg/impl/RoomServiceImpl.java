package com.pgman.service.pg.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.pg.RoomRepository;
import com.pgman.entities.pg.Room;
import com.pgman.service.pg.RoomService;

public class RoomServiceImpl implements RoomService{

    Logger LOGGER = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Autowired
    private RoomRepository roomRepo;

    @Override
    public Room addRoom(Room room) {
        Room r = null;
        try {
            r = roomRepo.save(room);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return r;
    }

    @Override
    public void deleteRoom(int id) {

       try {
            roomRepo.deleteById(id);
       } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
       }
        
    }

    @Override
    public Room getARoom(int id) {
        Room r = null;
        try {
            r = roomRepo.getReferenceById(id);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return r;
    }

    @Override
    public List<Room> getAllRoom() {
        return roomRepo.findAll();
    }

    @Override
    public Room updateRoom(int id, Room room) {
        Room r = null;
        try {
            r = roomRepo.save(room);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return r;
    }
    
}
