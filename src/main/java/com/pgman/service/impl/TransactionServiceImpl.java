package com.pgman.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.Transactions;
import com.pgman.service.GuestService;
import com.pgman.service.OwnerService;
import com.pgman.service.TransactionService;
import com.pgman.dao.TransactionsRepository;

public class TransactionServiceImpl implements TransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionsRepository transactionRepo;

    @Autowired
    private GuestService guestService;

    @Autowired
    private OwnerService ownerService;

    Owner owner = new Owner();

    Guest guest = new Guest();

    @Override
    public Transactions createTransaction(Transactions transaction) {
        owner = transaction.getOwner();
        // int amount = ownerService.getTotalCollectedRent(owner);
        int amount = owner.getTotalRentcollected();
        if (transaction.getPayments().isStatus()) {
            amount += transaction.getPayments().getAmount();
            owner.setTotalRentcollected(amount);
            ownerService.updateOwner(owner.getId(), owner);
        }
        return transactionRepo.save(transaction);
    }

    @Override
    public void deleteTransaction(int id) {
        try {
           transactionRepo.deleteById(id);;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}",e.getMessage());
        }
    }

    @Override
    public Transactions getATransation(int id) {
        Transactions tr = null;
        try {
            tr = transactionRepo.getReferenceById(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}",e.getMessage());
        }
        return tr;
    }

    @Override
    public List<Transactions> getAllTransaction() {
        List<Transactions> trs = null;
        try {
            trs = transactionRepo.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}",e.getMessage());
        }
        return trs;
    }

    @Override
    public List<Transactions> getTransactionByGuestAndOwner(Guest guest, Owner owner) {
        List<Transactions> trs = null;
        try {
            trs = transactionRepo.findByGuestAndOwner(guest, owner);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}",e.getMessage());
        }
        return trs;
    }

    @Override
    public Transactions updateTransaction(int id, Transactions transactions) {
        Transactions tr = null;
        try {
            tr = transactionRepo.save(transactions);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}",e.getMessage());
        }
        return tr;
    }

    @Override
    public List<Transactions> getTransactionByOwner(Owner owner) {
        List<Transactions> trs = null;
        try {
            trs = transactionRepo.findByOwner(owner);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}",e.getMessage());
        }
        return trs;
    }

    @Override
    public List<Transactions> getTransactionOfGuestAndOwner(Guest guest, Owner owner) {
        
        try {
            return transactionRepo.findByGuestAndOwner(guest, owner);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}",e.getMessage());
        }
        return null;
    }

    @Override
    public Page<Transactions> getSomeTransactionOfGuestAndOwner(Guest guest, Owner owner, Pageable pageable) {
        return transactionRepo.findByGuestAndOwner(guest, owner, pageable);
    }

    @Override
    public Page<Transactions> getSomeTransactionByOwner(Owner owner, Pageable pageable) {
        // return null;
        return transactionRepo.findByOwnerOrderByIdDesc(owner, pageable);
    }   
    
}
