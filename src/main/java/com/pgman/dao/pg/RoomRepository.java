package com.pgman.dao.pg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.pg.Room;

public interface RoomRepository extends JpaRepository<Room, Integer>{
    
}
