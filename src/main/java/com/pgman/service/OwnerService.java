package com.pgman.service;

import org.springframework.stereotype.Service;

import com.pgman.entities.Owner;
import com.pgman.entities.Guest;
import java.util.List;

@Service
public interface OwnerService {

    // 1. Create new owner
    public Owner creatOwner(Owner owner);

    // 2. Get all owner
    public List<Owner> getAllOwner();

    // 3. Get a single owner by id
    public Owner getwnerById(String id);

    // 4. Get a single owner by email (username)
    public Owner getOwnerByEmail(String emial);

    // 5. update owner by id
    public Owner updateOwner(String id, Owner owner);

    // 6. Delete a owner by id
    public void deleteOwner(String id);

    // 7. Exist or not find by email
    public boolean isExistByEmail(String email);

    // 8. Exist or not find by phone
    public boolean isExistByPhone(String phone);

    // 9. Get All Guest by owner
    public List<Guest> getAllGuest(Owner owner);

}
