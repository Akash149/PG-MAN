package com.pgman.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {

        public Page<Transactions> findByGuestAndOwner(Guest guest, Owner owner, Pageable pageable);

        // Get all payments of guest which paid to and owner
        public List<Transactions> findByGuestAndOwner(Guest guest, Owner owner);
   
        @Query("select tr from Transactions tr where transactionDate between :startDate and :endDate and tr.owner = :owner and tr.type = :type")
        public List<Transactions> getTransactionsOfCurrentMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("owner") Owner owner, String type);

        // Get all the transaction by owner
        public List<Transactions> findByOwner(Owner owner);

        // Get some transaction by owner (pageable)
        public Page<Transactions> findByOwnerOrderByIdDesc(Owner owner, Pageable pageable);
    
}
