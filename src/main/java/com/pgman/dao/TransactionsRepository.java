package com.pgman.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {

        // Get all payments of guest which paid to and owner
        public List<Transactions> findByGuestAndOwner(Guest guest, Owner owner);

        public List<Transactions> findByOwner(Owner owner);
    
}
