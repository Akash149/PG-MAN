package com.pgman.service.pg;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgman.entities.pg.Policy;

@Service
public interface PolicyService {
    
    // 1. Add/Create policy
    public Policy addPolicy(Policy policy);

    // 2. update policy
    public Policy updatePolicy(int id, Policy policy);

    // 3. Delete Policy by id
    public void deletePolicyById(int id);

    // 4. Delete Policy by policy
    public void deletePolicy(Policy policy);

    // 5. Get a single policy
    public Policy getAPolicyById(int id);

    // 6. Get all policy
    public List<Policy> getAllPolicy();

}
