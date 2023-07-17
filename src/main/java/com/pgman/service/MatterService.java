package com.pgman.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgman.entities.Matter;

@Service
public interface MatterService {

    public Matter createMatter(Matter matter);

    public List<Matter> getAllMatter();

    public Matter updateMatterById(int id, Matter matter) throws Exception;

    public Matter getAMatterById(int matterId);

    public void deleteAMatterById(int matterId);

    public void deleteMatter(Matter matter);
    
}
