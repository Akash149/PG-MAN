package com.pgman.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.Transactions;

@Service
public interface TransactionService {

    //1. Create a Transaction
    public Transactions createTransaction(Transactions transaction);

    //2. Get all transactions
    public List<Transactions> getAllTransaction();

    //3. Get a single Transaction
    public Transactions getATransation(int id);

    //4. Update Transaction by Id
    public Transactions updateTransaction(int id, Transactions transactions);

    //5. Delete a transaction
    public void deleteTransaction(int id);

    //6. Get Transaction by Guest and Owner
    public List<Transactions> getTransactionByGuestAndOwner(Guest guest, Owner owner);

    //7. Get Transaction by owner
    public List<Transactions> getTransactionByOwner(Owner owner);

    //8. Get Transactions by guest and owner
    public List<Transactions> getTransactionOfGuestAndOwner(Guest guest, Owner owner);

    // 9. Get some payments by guest and owner (Pageable)
    public Page<Transactions> getSomeTransactionOfGuestAndOwner(Guest guest, Owner owner, Pageable pageable);

    // 10. Get some transaction by guest
    public Page<Transactions> getSomeTransactionByOwner(Owner owner, Pageable pageable);

    // 11. Get list of transaction between date and purpose(current month)
    // public List<Transactions> getTransactionsBetween(Owner owner, Date startDate, Date endDate);
    public List<Transactions> getTransactionsBetween(LocalDate startDate, LocalDate endDate, Owner owner, String type);
    
}
