package com.pgman.dao.pg;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.pgman.entities.pg.Flat;
import com.pgman.entities.pg.Room;

public interface RoomRepository extends JpaRepository<Room, Integer>{

    public List<Room> findByFlat(Flat flat);
    
}
