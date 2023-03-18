package com.pgman.dao.pg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.pg.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    
}
