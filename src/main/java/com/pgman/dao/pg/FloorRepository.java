package com.pgman.dao.pg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.pg.Floor;

public interface FloorRepository extends JpaRepository<Floor, Integer> {
    
}
