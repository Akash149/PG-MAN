package com.pgman.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.AddressRepository;
import com.pgman.entities.Address;
import com.pgman.service.AddressService;

public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepo;

    private Address address;

    @Override
    public List<Address> allAddress() {
        return addressRepo.findAll();
    }

    @Override
    public Address createAddress(Address address) {
        return addressRepo.save(null);
    }

    @Override
    public void deleteAddress(int id) {
        Address add = addressRepo.findById(id).get();
        try {
            if (add != null) {
                addressRepo.deleteById(null);
            } else {
                throw new Exception("Resource Not Found");
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }    
    }

    @Override
    public Address getAddressById(int id) throws Exception {
        Address add = addressRepo.findById(id).get();
        if (add == null) {
            throw new Exception("Resource Not Found");
        } 
        return add;
    }

    @Override
    public Address updateAddress(int id, Address address) {
        this.address = addressRepo.findById(id).get();
        if (this.address != null ) {
            return this.addressRepo.save(address);
        } else {
        return null;
        }
    }

}
