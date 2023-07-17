package com.pgman.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.MatterRepository;
import com.pgman.entities.Matter;
import com.pgman.service.MatterService;

public class MatterServiceImpl implements MatterService {

    @Autowired
    MatterRepository matterRepo;

    @Override
    public Matter createMatter(Matter matter) {
        matterRepo.save(matter);
        return matter;
    }

    @Override
    public void deleteAMatterById(int matterId) {
        matterRepo.deleteById(matterId); 
    }

    @Override
    public void deleteMatter(Matter matter) {
        matterRepo.delete(matter);  
    }

    @Override
    public Matter getAMatterById(int matterId) {
        Matter matter = null;
        matter = matterRepo.getReferenceById(matterId);
        // if (matter != null)
        //     return matter;
        // else 
        //     throw new Exception("Matter is not avialable");
        return matter;
    }

    @Override
    public List<Matter> getAllMatter() {
        List<Matter> matters = matterRepo.findAll();
        return matters;
    }

    @Override
    public Matter updateMatterById(int id, Matter matter) throws Exception {
        Matter mat = matterRepo.getReferenceById(id);
        if (mat != null)
            matterRepo.save(matter);
        else 
            throw new Exception("Matter is not avialable");
        return mat;
    }

    
    
}
