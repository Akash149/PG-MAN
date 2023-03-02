package com.pgman.controller;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgman.dao.OwnerRepository;
import com.pgman.entities.Owner;
import com.pgman.entities.PgDetails;
import com.pgman.entities.Guest;
import com.pgman.entities.Transactions;
import com.pgman.service.ExcelService;
import com.pgman.service.PaymentService;
import com.pgman.service.TransactionService;
import com.pgman.service.PgService;

@Controller
@RequestMapping("/owner")
public class OwnerController {
    
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PgService pgService;

    @Autowired
    private ExcelService excelService;

    Logger logger = LoggerFactory.getLogger(OwnerController.class);

    // Owner assests
    private Owner owner;
    private List<PgDetails> pgs;
    private List<Transactions> transactions;
    private List<Guest> guests;

    @ModelAttribute
    public void commonData(Model model, Principal principal) {
        owner = ownerRepository.findByEmail(principal.getName());
        logger.info("Logged in Owner: " + owner.getName());

        pgs = pgService.getPgByOwner(owner);
        if (pgs != null) {
            logger.info("PGs are Loaded count is "+pgs.size());
        } else {
            logger.info("PGs are null");
        }
        
        transactions = transactionService.getTransactionByOwner(owner);
        if (transactions != null){
            logger.info("Owner Transactions are loaded, count is "+transactions.size());
        } else {
            logger.info("Owner Transactions are null");
        }

        model.addAttribute("pgs", pgs);
        model.addAttribute(owner);
        model.addAttribute("trans", transactions);
    }

    @GetMapping("/dashboard")
    public String ownerDashboard(Model model) {;
        model.addAttribute("title",owner.getName());
        return "ownerviews/ownerpage";
    }

    @GetMapping("/{id}/{name}")
    public String pgView(@PathVariable("id") String id, @PathVariable("name") String name, Model model) {
       try {
        PgDetails pgDetails = pgService.getPgById(id);
        if(pgDetails != null) {
            if(this.owner.getId().equals(pgDetails.getOwner().getId())) {
                guests = pgDetails.getGuest();
                logger.info("Guests are loaded count is "+guests.size());
                model.addAttribute("pg", pgDetails);
                model.addAttribute("gsts", guests);

                return "ownerviews/pgview";
            } else {
                return "redirect:/access-denied";
            } 
        }
       } catch (Exception e) {
        e.printStackTrace();
        
       }
       return "redirect:/error-404-not-found";
    }

    @PostMapping("/update/pg-details")
    public void updatePg(@ModelAttribute PgDetails pgDetails ) {
        System.out.println(pgDetails.toString());
    }

    // Download guest details in excel format
    @GetMapping("/download-guest-details")
    public ResponseEntity<Resource> download() {
        String filename = "guest_details.xlsx";
        try {
            ByteArrayInputStream data = excelService.getAllGuestByOwner(owner);
            InputStreamResource file = new InputStreamResource(data);
            logger.info("Excel file created, file name is " + filename);
            ResponseEntity<Resource> body = ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return null;     
    }
}
