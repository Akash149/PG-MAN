package com.pgman.service.pg.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.pg.FloorRepository;
import com.pgman.entities.Guest;
import com.pgman.entities.pg.Floor;
import com.pgman.entities.pg.PgDetails;
import com.pgman.service.GuestService;
import com.pgman.service.pg.FloorService;

public class FloorServiceImpl implements FloorService {

    Logger LOGGER = LoggerFactory.getLogger(FloorServiceImpl.class);

    @Autowired
    FloorRepository floorRepo;

    @Autowired
    GuestService guestService;

    @Override
    public Floor addFloor(Floor floor) {
        Floor f = null;
        try {
            f = floorRepo.save(floor);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteFloor(int id) {
        Floor floor = floorRepo.getReferenceById(id);
        for (Guest guest : floor.getGuest()) {
            guest.setFloor(null);
            guest.setFlat(null);
            guest.setRoom(null);
            guestService.updateGuest(guest.getId(), guest);
        }
        floor.setPgDetails(null);
        floorRepo.save(floor);
        floorRepo.delete(floor);  
    }

    @Override
    public Floor getAFloor(int id) {
        Floor f = null;
        f = floorRepo.getReferenceById(id);

        return f;
    }

    @Override
    public List<Floor> getAllFloor() {
        return floorRepo.findAll();
    }

    @Override
    public Floor updateFloor(int id, Floor floor) {
        Floor f = null;
        f = floorRepo.save(floor);

        return f;
    }

    @Override
    public List<Floor> getFloorsByPg(PgDetails pgDetails) {
        return floorRepo.findAllFloorByPgDetails(pgDetails);
    }
    
}
