package com.pgman.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

import com.pgman.entities.Address;
import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.pg.Room;

@Service
public interface GuestService {

    //1. save guest
    public Guest createGuest(Guest guest);

    //2. Get all guest
    public List<Guest> allGuest();

    //3. update a guest
    public Guest updateGuest(String id, Guest guest);

    //4. Delete a guest by id
    public void deleteGuest(String id);

    //5. Get a guest by id
    public Guest getGuestById(String id);

    //6. Get a guest by email or username
    public Guest getGuestByEmail(String email);

    //7. Exist or not find by email
    public boolean isExistByEmail(String email);

    //8. Exist or not find by email
    public boolean isExistByPhone(String phone);

    //9. update/add guest address
    public Guest updateAddress(String guestId, Address address);

    //10. Get recent guests pagination
    public Page<Guest> getRecentGuest(Owner owner, Pageable pageable);

    // 11. Get guest list by name
    public List<Guest> getSearchedGuest(String name,Owner owner);

    // 12. Get the count of total guest by owner
    public int getCountOfGuestByOwner(Owner owner)  throws Exception;

    // 13. Get the list of guest by owner and their room
    public List<Guest> getGuestsByRoom(Owner owner, Room room);

    // 14. Get the Total rent amout of pg by guest and owner
    public int getTotalRent(Owner owner);
}
