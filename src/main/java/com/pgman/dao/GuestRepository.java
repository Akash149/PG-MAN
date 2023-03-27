package com.pgman.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;

@Repository
public interface GuestRepository extends JpaRepository<Guest, String> {

    // Get guest by email (String)
    @Query("select g from Guest g where g.email = :email")
    public Guest findByEmail(@Param("email") String email);

    // Get guest by email (String)
    @Query("select g from Guest g where g.phone = :phone")
    public Guest findByPhone(@Param("phone") String phone);

    // Get recent 10 guests pagination
    @Query("from Guest as g where g.owner.id =:ownerId")
    public Page<Guest> findGuestByOwner(@Param("ownerId") String ownerId, Pageable pageable);

    // Search guest
    public List<Guest> findByNameContainingAndOwner(String name, Owner owner);
    
}
