package com.pgman.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgman.entities.Address;

@Service
public interface AddressService {

     //1. save Address;
     public Address createAddress(Address address);

     //2. Get all Address
     public List<Address> allAddress();
 
     //3. update a Address
     public Address updateAddress(int id, Address address);
 
     //4. Delete a Address by id
     public void deleteAddress(int id);
 
     //5. Get a Address by id
     public Address getAddressById(int id) throws Exception;

    
}
