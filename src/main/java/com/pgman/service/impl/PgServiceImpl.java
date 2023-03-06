package com.pgman.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.entities.Owner;
import com.pgman.entities.pg.PgDetails;
import com.pgman.service.PgService;
import com.pgman.dao.PgRepository;

public class PgServiceImpl implements PgService {

    @Autowired
    private PgRepository pgRepo;

    private PgDetails pgDetails;

    @Override
    public PgDetails addPg(PgDetails pgDetails) {
        return pgRepo.save(pgDetails);
    }

    @Override
    public void deletePgById(String id) {
        pgRepo.deleteById(id); 
    }

    @Override
    public List<PgDetails> getAllPg() {
        return pgRepo.findAll();
    }

    @Override
    public PgDetails getPgById(String id) {
        return pgRepo.findById(id).get();
    }

    @Override
    public PgDetails getPgByName(String pgName) {
        return pgRepo.findByName(pgName);
    }

    @Override
    public PgDetails updatePg(String id, PgDetails pgDetails) {
        this.pgDetails = pgRepo.findById(id).get();
        if (this.pgDetails != null) {
            return pgRepo.save(pgDetails);
        } else {
            return null;
        }
    }

    @Override
    public List<PgDetails> getPgByOwner(Owner owner) {
        List<PgDetails> pgs = null;
        try{
            pgs = pgRepo.findPgDetailsByOwner(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pgs;
    }

    
    
}
