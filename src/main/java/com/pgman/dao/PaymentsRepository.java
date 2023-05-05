package com.pgman.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.Payments;
import com.pgman.entities.Guest;

public interface PaymentsRepository extends JpaRepository<Payments, Integer> {
    
    // @Query("from Payments as p where p.Guest.id =:guestId")
    // public List<Payments> findPaymentsByGuest(@Param("guestId") Guest guest);

    // Get all payments of guest
    public List<Payments> findByGuest(Guest guest);

}
