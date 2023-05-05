package com.pgman.service.pg;

import org.springframework.stereotype.Component;

import com.pgman.entities.pg.Floor;
import com.pgman.entities.pg.PgDetails;

import java.util.List;

@Component
public interface FloorService {
    
    // 1. Create a new floor
    public Floor addFloor(Floor floor);

    // 2. Get all floor
    public List<Floor> getAllFloor();

    // 3. Get a single floor
    public Floor getAFloor(int id);

    // 4. Update a floor
    public Floor updateFloor(int id, Floor floor);

    // 5. delete a floor
    public void deleteFloor(int id);

    // 6. Get List of floor by pg
    public List<Floor> getFloorsByPg(PgDetails pgDetails);


}
