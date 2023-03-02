package com.pgman.config.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pgman.config.CustomUserDetails;
import com.pgman.dao.GuestRepository;
import com.pgman.dao.OwnerRepository;
import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Guest guest = guestRepository.findByEmail(username);
        Owner owner = ownerRepository.findByEmail(username);

        if (guest != null) {
            User userg = new User();
            userg.setEmail(guest.getEmail());
            userg.setPassword(guest.getPassword());
            userg.setRole(guest.getRole());
            CustomUserDetails customUserDetails = new CustomUserDetails(userg);
            System.out.println(guest);
            System.out.println(userg);

            return customUserDetails;

        } else if ( owner != null) {
            User usero = new User() ;
            usero.setEmail(owner.getEmail());
            usero.setPassword(owner.getPassword());
            usero.setRole(owner.getRole());
            CustomUserDetails customUserDetails = new CustomUserDetails(usero);
            System.out.println(owner);
            System.out.println(usero);
            
            return customUserDetails;
        } else {
            throw new UsernameNotFoundException("Could not found user !");
        }

    }
    
}
