package com.pgman.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.pg.Room;

public interface GuestRepository extends JpaRepository<Guest, String> {

    // Get guest by email (String)
    @Query("select g from Guest g where g.email = :email")
    public Guest findByEmail(@Param("email") String email);

    // Get guest by email (String)
    @Query("select g from Guest g where g.phone = :phone")
    public Guest findByPhone(@Param("phone") String phone);

    // Get recent 10 guests pagination
    // @Query("from Guest as g where g.owner.id =:ownerId")
    // public Page<Guest> findByOwnerOrderByIdDesc(@Param("ownerId") String ownerId, Pageable pageable);

    public Page<Guest> findByOwnerOrderByIdDesc(Owner owner, Pageable pageable);

    // Search guest
    public List<Guest> findByNameContainingAndOwner(String name, Owner owner);
    public List<Guest> findByNameContainingIgnoreCaseAndOwner(String name, Owner owner);

    // @Query("select count(g.guest) from owner where owner = :owner")
    public int countGuestByOwner(Owner owner);

    @Query("select g from Guest g where g.room = :room")
    public List<Guest> findByOwner(@Param("room") Room room);

    // get the list of guest by owner and their room
    public List<Guest> findByOwnerAndRoom(Owner owner, Room room);

    @Query("select sum(g.rentAmount) from Guest g where g.owner = :owner")
    public int getTotalRentAmount(@Param("owner") Owner owner);
    
}
