package com.pgman.dao.pg;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.pg.Flat;
import com.pgman.entities.pg.Floor;

public interface FlatRepository extends JpaRepository<Flat, Integer> {

    public List<Flat> findAllFlatByFloor(Floor floor);
    
}
