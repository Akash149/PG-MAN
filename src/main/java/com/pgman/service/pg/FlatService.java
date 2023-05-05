package com.pgman.service.pg;

import org.springframework.stereotype.Service;

import com.pgman.entities.pg.Flat;
import com.pgman.entities.pg.Floor;

import java.util.List;

@Service
public interface FlatService {

    // 1. Create/Add a Flat
    public Flat addFlat(Flat flat);

    // 2. get All flat 
    public List<Flat> getAllFlat();

    // 3. update a Flat
    public Flat updateFlat(int id, Flat flat);

    // 4. Delete a flat
    public void deleteFlat(int id);

    // 5. get a single flat
    public Flat getAFlat(int id);

    // 6. Get all flat by floor
    public List<Flat> getFlatByFloor(Floor floor);

    
}
