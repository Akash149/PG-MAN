package com.pgman.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgman.entities.Matter;

@Repository
public interface MatterRepository extends JpaRepository<Matter, Integer> {
    
}
