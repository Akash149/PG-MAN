package com.pgman.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgman.entities.PgDetails;
import com.pgman.entities.Owner;

public interface PgRepository extends JpaRepository<PgDetails,String> {

    //1.  Find by name
    @Query("select pg from PgDetails pg where pg.name = :pgName")
    public PgDetails findByName(@Param("pgName") String pgName);

    public List<PgDetails> findPgDetailsByOwner(Owner owner);
    
}
