package com.pgman.service.pg.impl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.pg.FlatRepository;
import com.pgman.entities.pg.Flat;
import com.pgman.entities.pg.Floor;
import com.pgman.service.pg.FlatService;

public class FlatServiceImpl implements FlatService {

    Logger LOGGER = LoggerFactory.getLogger(FlatServiceImpl.class);

    @Autowired
    FlatRepository flatRepo;

    @Override
    public Flat addFlat(Flat flat) {
        flat.setAddedDate(LocalDate.now());
        flat.setStatus(true);
        Flat f = flatRepo.save(flat);
        return f;
    }

    @Override
    public void deleteFlat(int id) {
        flatRepo.deleteById(id);
    }

    @Override
    public Flat getAFlat(int id) {
        return flatRepo.getReferenceById(id);
    }

    @Override
    public List<Flat> getAllFlat() {
        return flatRepo.findAll();
    }

    @Override
    public Flat updateFlat(int id, Flat flat) {
        Flat f = flatRepo.save(flat);
        return f;
    }

    @Override
    public List<Flat> getFlatByFloor(Floor floor) {
        return flatRepo.findAllFlatByFloor(floor);
    }
  
    
}
