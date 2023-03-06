package com.pgman.service.pg.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.pg.FlatRepository;
import com.pgman.entities.pg.Flat;
import com.pgman.service.pg.FlatService;

public class FlatServiceImpl implements FlatService {

    Logger LOGGER = LoggerFactory.getLogger(FlatServiceImpl.class);

    @Autowired
    FlatRepository flatRepo;

    @Override
    public Flat addFlat(Flat flat) {
        Flat f = null;
        try {
            f = flatRepo.save(flat);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteFlat(int id) {
        try {
            flatRepo.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        
    }

    @Override
    public Flat getAFlat(int id) {
        Flat f = null;
        try {
            f = flatRepo.getReferenceById(id);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return null;
    }

    @Override
    public List<Flat> getAllFlat() {
        return flatRepo.findAll();
    }

    @Override
    public Flat updateFlat(int id, Flat flat) {
        Flat f = null;
        try {
            f = flatRepo.save(flat);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
        }
        return f;
    }
    
}
