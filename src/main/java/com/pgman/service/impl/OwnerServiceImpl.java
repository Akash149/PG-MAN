package com.pgman.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.Payments;
import com.pgman.entities.Transactions;
import com.pgman.helper.IdCreator;
import com.pgman.service.GuestService;
import com.pgman.service.OwnerService;
import com.pgman.dao.OwnerRepository;

public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private Owner owner;

    @Override
    public Owner creatOwner(Owner owner) {
        String id = IdCreator.createId("OWNER");
        owner.setId(id);
        owner.setProfile("default.png");
        return ownerRepository.save(owner);
    }

    @Override
    public void deleteOwner(String id) {
        this.ownerRepository.deleteById(id);
    }

    @Override
    public List<Owner> getAllOwner() {
        return this.ownerRepository.findAll();
    }

    @Override
    public Owner getOwnerByEmail(String emial) {
        return this.ownerRepository.findByEmail(emial);
    }

    @Override
    public Owner getwnerById(String id) {
        return this.ownerRepository.findById(id).get();
    }

    @Override
    public Owner updateOwner(String id, Owner owner) {
        this.owner = ownerRepository.findById(id).get();
        if (this.owner != null) {
            return this.ownerRepository.save(owner);
        } else {
            return null;
        }
    }

    @Override
    public boolean isExistByEmail(String email) {
        Owner owner = this.ownerRepository.findByEmail(email);
        if (owner != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExistByPhone(String phone) {
        Owner owner = this.ownerRepository.findByPhone(phone);
        if (owner != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<Guest> getAllGuest(Owner owner) {
        Owner own = this.ownerRepository.findById(owner.getId()).get();
        List<Guest> g = own.getGuest();
        return g;
    }

    @Override
    public int getTotalCollectedRent(Owner owner) {
        // TODO Auto-generated method stub
        List<Transactions> transactions = owner.getTransactions();
        List<Payments> payments = new ArrayList<Payments>();
        int totalAmount = 0;
        for (Transactions tr : transactions) {
                payments.add(tr.getPayments());
        }

        for (Payments pmt : payments) {
            totalAmount += pmt.getAmount();
        }
        return totalAmount;
    }

    @Override
    public int getTotalCollectedRentOfCurrentMonth(Owner owner) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getTotalGuest(Owner owner) {
        // TODO Auto-generated method stub
        return 0;
    } 

}
