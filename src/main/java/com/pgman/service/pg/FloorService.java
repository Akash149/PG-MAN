package com.pgman.service.pg;

import org.springframework.stereotype.Service;

import com.pgman.entities.pg.Floor;

import java.util.List;

@Service
public interface FloorService {
    
    //1. Create a new floor
    public Floor addFloor(Floor floor);

    //2. Get all floor
    public List<Floor> getAllFloor();

    // 3. Get a single floor
    public Floor getAFloor(int id);

    //4. Update a floor
    public Floor updateFloor(int id, Floor floor);

    //5. delete a floor
    public void deleteFloor(int id);


}
