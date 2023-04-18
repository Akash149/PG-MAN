package com.pgman.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgman.dao.AddressRepository;
import com.pgman.dao.GuestRepository;
import com.pgman.entities.Address;
import com.pgman.entities.Guest;
import com.pgman.entities.Payments;
import com.pgman.helper.Message;
import com.pgman.helper.SaveDocFile;
import com.pgman.service.GuestService;
import com.pgman.service.PaymentService;
import com.pgman.service.TransactionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/guest")
public class GuestController {

    // Logger factory SL4j
    Logger logger = LoggerFactory.getLogger(GuestController.class);

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private GuestService guestService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    private AddressRepository addressRepository;

    private Guest guest;

    // Common data
    @ModelAttribute
    public void commonData(Model model, Principal principal,HttpSession session, HttpServletRequest request) {
        logger.info("This is Guest Controller");
        guest = guestRepository.findByEmail(principal.getName());
        
        try {
            List<Payments> payments = paymentService.getAllPaymentByGuest(guest);
            model.addAttribute("payment", payments);
        } catch (Exception e) {
            model.addAttribute("payment", new Payments());
            e.printStackTrace();
        }

        if (guest.getAddress() == null) {
            guest.setAddress(new Address());
        }

        if (guest.getAddress() !=null || guest.isEnabled() == false) {
            session.setAttribute("message", 
            new Message("success","You have added your details. please, wait for owner's verification !!"));
        }

        model.addAttribute(guest);
        System.out.println(guest);
    }

    @GetMapping("/dashboard")
    public String guestDashboard(Model model) {
        model.addAttribute("title", guest.getName());
        return "guestviews/guestpage";
    } 

    // Add document and address details
    @PostMapping("/add-details")
    public String addDetails(@ModelAttribute Address address, 
    @RequestParam("document") MultipartFile file, 
    RedirectAttributes rat, HttpServletRequest request ) {
        // System.out.println(address);
        try {
            guest.setAddress(address);
            if(file.isEmpty()) {
                rat.addFlashAttribute("error","file is empty"); 
            } else {
                String filename = SaveDocFile.savefile("GUEST", "doc", 
                guest.getName(), guest.getId(), file); 
                guest.setDocument(filename);
                logger.info("Files are uploaded");
                guestService.updateGuest(guest.getId(), guest);     
                rat.addFlashAttribute("success","Details are saved"); 
                logger.info("Details are saved");    
            }
            return "redirect:/guest/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}", e);
        }
        return "redirect:/guest/dashboard";
    }

    // Edit guest profile 
    @GetMapping("/profile")
    public String guestProfile(Model model) {
        model.addAttribute("title", guest.getName());
        return "guestviews/guestprofile";
    }

    // upload guest profile picture
    @PostMapping("/upload/dp")
    public String uploadProfileImage(MultipartFile file) {
        try {
            String filename = SaveDocFile.savefile("GUEST", "DP", 
            guest.getName(), guest.getId(), file); 
            guest.setProfile(filename);
            logger.info("Files are uploaded");
            logger.info("{}",guest.getName() + "changed own dp.");
            guestService.updateGuest(guest.getId(), guest);      
            logger.info("Details are saved"); 
            return "Done";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Error";
        }

    }

    // update guest profile
    @PostMapping("/update-details")
    public String updateGuest(@ModelAttribute Guest guest, 
    @RequestParam("dp") MultipartFile file, RedirectAttributes rat) {

        try {
            String filename = SaveDocFile.savefile("GUEST", "DP", 
            guest.getName(), guest.getId(), file);
            if(!filename.isEmpty()) {
                guest.setProfile(filename);
            } else {
                guest.setProfile(guest.getProfile());
            }
            guestService.updateGuest(guest.getId(), guest);
            logger.info("Update successfully");
            rat.addFlashAttribute("success","Updated successfully");
            return "redirect:/guest/dashboard";
        } catch (Exception e) {
           logger.error("{}", e.getMessage());
           rat.addFlashAttribute("error","Something went wrong on Server !!");
           return "redirect:/guest/dashboard";
        }
    }
}
