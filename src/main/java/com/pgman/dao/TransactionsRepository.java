package com.pgman.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {

        public Page<Transactions> findByGuestAndOwner(Guest guest, Owner owner, Pageable pageable);

        // Get all payments of guest which paid to and owner
        public List<Transactions> findByGuestAndOwner(Guest guest, Owner owner);

        // Get all the transaction by owner
        public List<Transactions> findByOwner(Owner owner);

        // Get some transaction by owner (pageable)
        public Page<Transactions> findByOwnerOrderByIdDesc(Owner owner, Pageable pageable);
    
}
