package com.pgman.service.pg.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.pg.FloorRepository;
import com.pgman.entities.pg.Floor;
import com.pgman.service.pg.FloorService;

public class FloorServiceImpl implements FloorService {

    Logger LOGGER = LoggerFactory.getLogger(FloorServiceImpl.class);

    @Autowired
    FloorRepository floorRepo;

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
        try {
            floorRepo.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("{}",e.getMessage());
        }
        
    }

    @Override
    public Floor getAFloor(int id) {
        Floor f = null;
        try {
            f = floorRepo.getReferenceById(id);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return f;
    }

    @Override
    public List<Floor> getAllFloor() {
        return floorRepo.findAll();
    }

    @Override
    public Floor updateFloor(int id, Floor floor) {
        Floor f = null;
        try {
            f = floorRepo.save(floor);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return f;
    }
    
}
