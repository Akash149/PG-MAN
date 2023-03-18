package com.pgman.service.pg.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.entities.pg.Policy;
import com.pgman.service.pg.PolicyService;

import jakarta.transaction.Transactional;

import com.pgman.dao.pg.PolicyRepository;

public class PolicyServiceImpl implements PolicyService {

    Logger LOGGER = LoggerFactory.getLogger(PolicyServiceImpl.class);

    @Autowired
    PolicyRepository policyRepo;

    @Override
    public Policy addPolicy(Policy policy) {
        Policy pol = null;
        try {
            pol = policyRepo.save(policy);
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
        return pol;
    }

    @Override
    public void deletePolicy(Policy policy) {
        try {
            policyRepo.delete(policy);
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }  
    }

    @Override
    public void deletePolicyById(int id) {
        
        try {
            policyRepo.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
        
    }

    @Override
    public Policy getAPolicyById(int id) {
        Policy pol = null;
        try {
            pol = policyRepo.getReferenceById(id);
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
        return pol;
    }

    @Override
    public List<Policy> getAllPolicy() {
        return policyRepo.findAll();
    }

    @Override
    @Transactional
    public Policy updatePolicy(int id, Policy policy) {
        Policy pol = null;
        try {
            pol = policyRepo.getReferenceById(id);
            pol = policyRepo.save(policy);
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
        return pol;
    }
    
}
