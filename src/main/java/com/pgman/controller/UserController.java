package com.pgman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgman.entities.Guest;
import com.pgman.service.GuestService;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/user/register")
    public Guest registerUser(@ModelAttribute() Guest guest) {
        System.out.println("This is usercontroller");
        System.out.println(guest);
        try {
            Guest guest1 = this.guestService.createGuest(guest);
            return guest1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    

}
