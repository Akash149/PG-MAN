package com.pgman.dao.pg;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.pg.Flat;

public interface FlatRepository extends JpaRepository<Flat, Integer> {
    
}
