package com.pgman.service.impl;

import com.pgman.service.GuestService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pgman.dao.AddressRepository;
import com.pgman.dao.GuestRepository;
import com.pgman.entities.Address;
import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.helper.IdCreator;

public class GuestServiceImpl implements GuestService{

    static final Logger logger  = LoggerFactory.getLogger(GuestServiceImpl.class);
    
    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Guest guest;

    @Override
    public Guest createGuest(Guest guest) {
        try {
            guest.setId(IdCreator.createId("GUEST"));
            guest.setProfile("default.png");
            Guest us = this.guestRepository.save(guest);
            return us;
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Guest> allGuest() {
        return guestRepository.findAll();
    }

    @Override
    public void deleteGuest(String id) {
        try {
            guestRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
        }
        
    }

    @Override
    public Guest getGuestByEmail(String email) {
        try {
            return guestRepository.findByEmail(email);
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            return null;
        }
    }

    @Override
    public Guest getGuestById(String id) {
        Guest guest = guestRepository.findById(id).get();
        if (guest != null) {
            return guest;
        } else {
            logger.warn("{} {}", id, "not Found in repository");
            return null;
        }   
    }

    @Override
    public Guest updateGuest(String id, Guest guest) {
        this.guest = guestRepository.findById(id).get();
        if (guest != null) {
            return this.guestRepository.save(guest);
        } else {
            logger.warn("{} {}", id, "not Found in repository");
            return null;
        }
    }

    @Override
    public boolean isExistByEmail(String email) {
        Guest guest = this.guestRepository.findByEmail(email);
        if(guest != null) {
            return true;
        }
        logger.warn("{} {}", email, "not Found in repository");
        return false;
    }

    @Override
    public boolean isExistByPhone(String phone) {
        Guest guest = this.guestRepository.findByPhone(phone);
        if(guest != null) {
            System.out.println(guest);
            return true;
        }
        logger.warn("{} {}", phone, "not Found in repository");
        return false;
    }

    @Override
    public Guest updateAddress(String guestId, Address address) {
        this.guest = guestRepository.findById(guestId).get();
        if (this.guest != null) {
            Address ad = addressRepository.findById(guest.getAddress().getId()).get();
            if( ad != null) {
                this.guest.setAddress(address);
                guestRepository.save(this.guest);     
            }
            return this.guest;
        } else {
            logger.warn("{} {}", guestId, "not Found in repository");
            return null;
        }
        
    }

    @Override
    public Page<Guest> getRecentGuest(String ownerId, Pageable pageable) {
        return guestRepository.findGuestByOwner(ownerId, pageable);
    }

    @Override
    public List<Guest> getSearchedGuest(String name, Owner owner) {
        guestRepository.findByNameContainingAndOwner(name, owner);
        return null;
    }

}
