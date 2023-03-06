package com.pgman.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgman.entities.Owner;
import com.pgman.entities.pg.PgDetails;

@Service
public interface PgService {

    //1. create new PG
    public PgDetails addPg(PgDetails pgDetails);

    //2. update a PG
    public PgDetails updatePg(String id, PgDetails pgDetails);

    //3. Get All PG
    public List<PgDetails> getAllPg();

    //4. Get a single pg by name
    public PgDetails getPgByName(String pgName);

    //5. Get a single pg by id 
    public PgDetails getPgById(String id);

    //6. Delete a single pg by ID
    public void deletePgById(String id);

    //7. Get PG by Owner
    public List<PgDetails> getPgByOwner(Owner owner);
    
}
