package com.pgman.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgman.entities.Guest;

public interface GuestRepository extends JpaRepository<Guest, String> {

    // Get guest by email (String)
    @Query("select g from Guest g where g.email = :email")
    public Guest findByEmail(@Param("email") String email);

    // Get guest by email (String)
    @Query("select g from Guest g where g.phone = :phone")
    public Guest findByPhone(@Param("phone") String phone);
    
}
