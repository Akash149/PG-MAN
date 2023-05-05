package com.pgman.dao.pg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.pg.Floor;
import com.pgman.entities.pg.PgDetails;

public interface FloorRepository extends JpaRepository<Floor, Integer> {

    public List<Floor> findAllFloorByPgDetails(PgDetails pgDetails);
    
}
