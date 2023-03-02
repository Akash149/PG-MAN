package com.pgman.service.impl;

import com.pgman.service.GuestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.AddressRepository;
import com.pgman.dao.GuestRepository;
import com.pgman.entities.Address;
import com.pgman.entities.Guest;
import com.pgman.helper.IdCreator;

public class GuestServiceImpl implements GuestService{
    
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
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Guest> allGuest() {
        return guestRepository.findAll();
    }

    @Override
    public void deleteGuest(String id) {
        guestRepository.deleteById(id);
    }

    @Override
    public Guest getGuestByEmail(String email) {
        return guestRepository.findByEmail(email);
    }

    @Override
    public Guest getGuestById(String id) {
        Guest guest = guestRepository.findById(id).get();
        if (guest != null) {
            return guest;
        } else {
            return null;
        }   
    }

    @Override
    public Guest updateGuest(String id, Guest guest) {
        this.guest = guestRepository.findById(id).get();
        if (guest != null) {
            return this.guestRepository.save(guest);
        } else {
            return null;
        }
    }

    @Override
    public boolean isExistByEmail(String email) {
        Guest guest = this.guestRepository.findByEmail(email);
        if(guest != null) {
            System.out.println(guest);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExistByPhone(String phone) {
        Guest guest = this.guestRepository.findByPhone(phone);
        if(guest != null) {
            System.out.println(guest);
            return true;
        }
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
            return null;
        }
        
    }

}
