package com.pgman.controller;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgman.dao.OwnerRepository;
import com.pgman.entities.Owner;
import com.pgman.entities.Payments;
import com.pgman.entities.PgUtilities;
import com.pgman.entities.Guest;
import com.pgman.entities.Transactions;
import com.pgman.entities.pg.Flat;
import com.pgman.entities.pg.Floor;
import com.pgman.entities.pg.PgDetails;
import com.pgman.entities.pg.Policy;
import com.pgman.entities.pg.Room;
import com.pgman.service.ExcelService;
import com.pgman.service.GuestService;
import com.pgman.service.OwnerService;
import com.pgman.service.PaymentService;
import com.pgman.service.TransactionService;
import com.pgman.service.pg.FlatService;
import com.pgman.service.pg.FloorService;
import com.pgman.service.pg.PolicyService;
import com.pgman.service.pg.RoomService;
import com.pgman.service.PgService;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private PgService pgService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private FlatService flatService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private PgUtilities pgUtilities;

    Logger logger = LoggerFactory.getLogger(OwnerController.class);

    // Owner assests
    private Owner owner;
    private List<PgDetails> pgs;
    // private List<Transactions> transactions;
    private Page<Transactions> transactions;
    private List<Guest> guests;

    @ModelAttribute
    public void commonData(Model model, Principal principal) {
        owner = ownerService.getOwnerByEmail(principal.getName());
        logger.info("Logged in Owner: {} ", owner.getName());

        pgs = pgService.getPgByOwner(owner);
        if (pgs != null) {
            logger.info("PGs are Loaded count is {}", pgs.size());
        } else {
            logger.info("PGs are null");
        }

        // transactions = transactionService.getTransactionByOwner(owner);

        try {
            owner.setTotalGuest(guestService.getCountOfGuestByOwner(owner));
        } catch (Exception e) {
            e.printStackTrace();
        logger.error("{}", e.getMessage());
        }
        model.addAttribute("pgs", pgs);
        model.addAttribute(owner);
    }

    @GetMapping("/dashboard")
    public String ownerNewDashboard(Model model) {
        // get recent 5 guests
        Pageable pageable = PageRequest.of(0, 5);
        Page<Guest> guests = guestService.getRecentGuest(owner, pageable);
        Pageable pagesize = PageRequest.of(0,14);
        int percent = pgUtilities.getRentCollectionPercentage(owner);
        transactions = transactionService.getSomeTransactionByOwner(owner,pagesize);
        logger.info("Owner Transactions are loaded, count is {}", transactions.getSize());
        model.addAttribute("rguest", guests);
        model.addAttribute("title", owner.getName());
        model.addAttribute("trans", transactions);
        model.addAttribute("percentage", percent);
        model.addAttribute("totalRent", pgUtilities.getTotalRent());
        model.addAttribute("collected",  pgUtilities.getCollectedAmount());
        model.addAttribute("remaining",  pgUtilities.getRemainingAmount());
        return "ownerviews/ownerdash";
    }

    // It shows the main view of pg and their details
    @GetMapping("/{id}/{name}")
    public String pgView(@PathVariable("id") String id, @PathVariable("name") String name, Model model) {
        List<Guest> unallocGuest = null;
        try {
            PgDetails pgDetails = pgService.getPgById(id);
            if (pgDetails != null) {
                if (this.owner.getId().equals(pgDetails.getOwner().getId())) {
                    guests = pgDetails.getGuest();
                    unallocGuest = guestService.getGuestsByRoom(owner, null);
                    logger.info("Guests are loaded count is " + guests.size());
                    model.addAttribute("pg", pgDetails);
                    model.addAttribute("gsts", guests);
                    model.addAttribute("unalloc", unallocGuest);
                    logger.info("UnAllocated guest count is {}",unallocGuest.size());
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

    // Download guest details in excel format
    @GetMapping("/download-guest-details")
    public ResponseEntity<Resource> download() {
        String filename = "guest_details.xlsx";
        try {
            ByteArrayInputStream data = excelService.getAllGuestByOwner(owner);
            InputStreamResource file = new InputStreamResource(data);
            logger.info("Excel file created, file name is " + filename);
            ResponseEntity<Resource> body = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    // Get guest-detail of a particular pg
    @GetMapping("/{pgId}/{guestId}/{guestName}")
    public String getGuestDetails(@PathVariable String pgId,
            @PathVariable String guestId, @PathVariable String guestName, Model model) {

        PgDetails pg = null;
        Guest guest = null;
        List<Transactions> trs = null;
        List<Payments> payments = new ArrayList<Payments>();


        try {
            // ! this logic will remove in future
            for (PgDetails p : pgs) {
                if (p.getId().equals(pgId)) {
                    pg = p;
                    guest = guestService.getGuestById(guestId);
                    trs = transactionService.getTransactionOfGuestAndOwner(guest, owner);

                    // Get the payment from transaction and add into list of payments.
                    for (Transactions tr : trs) {
                        payments.add(tr.getPayments());
                    }
                }
            }

            if (pg.getOwner().getId().equals(this.owner.getId())) {
                List<Floor> floors = floorService.getFloorsByPg(guest.getPgDetails());
                model.addAttribute("floors", floors);
                model.addAttribute("title", guest.getName());
                model.addAttribute("guest", guest);
                model.addAttribute("payment", payments);
                return "ownerviews/guestdetails";
            }

            else {
                return "redirect:/access-denied";
            }

        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            return "redirect:/error-404-not-found";
        }
    }

    // Add a specific PG's policy
    @PostMapping("/add/{pgid}/policy")
    public String addPgPolicy(@ModelAttribute Policy policy,
            @PathVariable String pgid, RedirectAttributes rat) {

        try {
            PgDetails pg = pgService.getPgById(pgid);
            if (pg.getOwner().getId().equals(owner.getId())) {
                policyService.addPolicy(policy);
                pg.setPolicy(policy);
                pgService.updatePg(pgid, pg);

                rat.addFlashAttribute("success", "Policy is added successfully");
                return "redirect:/owner/" + pg.getId() + "/" + pg.getName().replace(" ", "-");
            } else {
                return "redirect:/access-denied";
            }
        } catch (Exception e) {
            rat.addFlashAttribute("error", "Something went error on server !!");
            return "redirect:/error-404-not-found";
        }
    }

    // Add a specific PG's policy
    @PostMapping("/update/{pgid}/policy")
    public String updatePgPolicy(@ModelAttribute Policy policy,
            @PathVariable String pgid, RedirectAttributes rat) {

        try {
            PgDetails pg = pgService.getPgById(pgid);
            if (pg.getOwner().getId().equals(owner.getId())) {
                // policyService.updatePolicy(pg.getPolicy().getId(), policy);
                pg.setPolicy(policy);
                pgService.updatePg(pgid, pg);

                rat.addFlashAttribute("success", "Policy is updated successfully");
                return "redirect:/owner/" + pg.getId() + "/" + pg.getName().replace(" ", "-");
            } else {
                return "redirect:/access-denied";
            }
        } catch (Exception e) {
            rat.addFlashAttribute("error", "Something went error on server !!");
            return "redirect:/error-404-not-found";
        }
    }

    // Add/Update/Delete PG and their details
    @GetMapping("/{pgId}/{pgName}/details")
    public String viewPg(@PathVariable("pgId") String id, Model model) {
        PgDetails pgd = null;
        try {
            pgd = pgService.getPgById(id);

            if (pgd.getOwner().getId().equals(owner.getId())) {
                model.addAttribute("pg", pgd);
                return "ownerviews/pgdetails";
            } else {
                return "redirec:/access-denied";
            }

        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            return "redirect:/error-404-not-found";
        }
    }

    // Update PG details
    @PostMapping("/update/pg-details")
    public void updatePg(@ModelAttribute PgDetails pgDetails) {

    }

    // Add floor handler
    @PostMapping("/{pgId}/add/floor")
    public String addFloor(@ModelAttribute Floor floor,
            @PathVariable("pgId") String id, Model model, RedirectAttributes rat) {
        PgDetails pgd = null;
        try {
            pgd = pgService.getPgById(id);

            if (pgd.getOwner().getId().equals(owner.getId())) {
                floor.setPgDetails(pgd);
                floor.setStatus(true);
                List<Floor> fl = new ArrayList<Floor>();
                fl.add(floor);
                floorService.addFloor(floor);
                pgd.setFloor(fl);
                pgService.updatePg(id, pgd);
                model.addAttribute("pg", pgd);
                rat.addFlashAttribute("success", "Floor " + floor.getName() + "Added sucessfully.");
                logger.info("{}", "Floor " + floor.getName() + " is added");
                return "ownerviews/pgdetails";
            } else {
                rat.addFlashAttribute("error", "Something went wrong on server  !!");
                return "ownerviews/pgdetails";
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            rat.addFlashAttribute("error", "Something went wrong on server  !!");
            return "ownerviews/pgdetails";
        }
    }

    // Delete floor by pg owner
    @GetMapping("/delete/{fid}")
    public String deleteFloor(@PathVariable("fid") int fid, RedirectAttributes rat) {
        logger.info("Are you want to delted this floor");

        try {
            Floor flr = floorService.getAFloor(fid);

            if (flr.getPgDetails().getOwner().getId().equals(this.owner.getId())) {
                List<Flat> flats = flr.getFlat();
                List<Guest> guests = flr.getGuest();
                for (Flat f : flats) {
                    // f.setFloor(null);
                    // flatService.updateFlat(f.getId(), f);
                    flatService.deleteFlat(f.getId());
                }

                for (Guest g : guests) {
                    g.setFloor(null);
                    guestService.updateGuest(g.getId(), g);
                }
                floorService.deleteFloor(fid);
                logger.info("Floor {}", flr.getName() + "is deleted ");
                rat.addFlashAttribute("success", "Floor " + flr.getName() + " is deleted");
                return "redirect:/owner/dashboard";
            } else {
                return "redirect:/access-denied";
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            rat.addFlashAttribute("error", "Something went wrong on server !");
            return "redirect:/owner/dashboard";
        }
    }

    // Activate the guest account by owner
    @GetMapping("/{pgId}/{guestId}/activate")
    public String activateGuest(@PathVariable("pgId") String pgId,
            @PathVariable("guestId") String guestId, RedirectAttributes rat) {
        Guest guest = null;
        try {
            guest = guestService.getGuestById(guestId);

            if (guest.getPgDetails().getOwner().getId().equals(this.owner.getId())) {
                guest.setEnabled(true);
                guestService.updateGuest(guestId, guest);
                logger.info("{}", guest.getName() + " is activated now");
                rat.addFlashAttribute("success", guest.getName() + " is activated");
                return "redirect:/owner/" + pgId + "/" + guestId + "/" + guest.getName().replace(" ", "-");
            } else {
                return "redirect:/access-denied";
            }
        } catch (Exception e) {
            rat.addFlashAttribute("error", "Something went wrong at server !!");
            logger.error("{}", e.getMessage());
            return "redirect:/owner/dashboard";
        }
    }

    // Deactivate the guest account by owner
    @GetMapping("/{pgId}/{guestId}/deactivate")
    public String deactivateGuest(@PathVariable("pgId") String pgId,
            @PathVariable("guestId") String guestId, RedirectAttributes rat) {
        Guest guest = null;
        try {
            guest = guestService.getGuestById(guestId);

            if (guest.getPgDetails().getOwner().getId().equals(this.owner.getId())) {
                guest.setEnabled(false);
                guestService.updateGuest(guestId, guest);
                logger.info("{}", guest.getName() + " is deactivated now");

                rat.addFlashAttribute("success", guest.getName() + " is deactivated");
                return "redirect:/owner/" + pgId + "/" + guestId + "/" + guest.getName().replace(" ", "-");
            } else {
                return "redirect:/access-denied";
            }
        } catch (Exception e) {
            rat.addFlashAttribute("error", "Something went wrong at server !!");
            logger.error("{}", e.getMessage());
            return "redirect:/owner/dashboard";
        }
    }

    // Cash collect by owner
    @PostMapping("/collect-cash")
    public String collectCash(@RequestParam("guestId") String guestId,
            @RequestParam("amount") int amount, @RequestParam("collectiontype") String collType, RedirectAttributes rat) {
        Guest guest = null;
        try {
            guest = guestService.getGuestById(guestId);
            if (amount > 0) {
                // create a new payment
                Payments payment = new Payments();
                payment.setAmount(amount);
                payment.setDate(LocalDate.now());
                payment.setGateway("CASH");
                payment.setRefNo(null);
                payment.setStatus(true);
                payment.setGuest(guest);
                paymentService.createPayment(payment);
                logger.info("{}", "Payments of " + amount + " is saved");

                // create a new transaction between guest and owner
                Transactions transactions = new Transactions();
                transactions.setGuest(guest);
                transactions.setOwner(this.owner);
                transactions.setPayments(payment);
                transactionService.createTransaction(transactions);

                if(collType.equals("rent")) {
                    guest.setPaidAmount(amount);
                    guest.setRemainingAmount(guest.getRemainingAmount() - amount);
                    guest.setPaymentStatus(true);
                    guestService.updateGuest(guestId, guest);
                    logger.info(guest.getName() + " has been paid their rent, collected amount is " + amount);
                }

                if(collType.equals("advance")) {
                    guest.setAdvancePaid(guest.getAdvancePaid() + amount);
                    guestService.updateGuest(guestId, guest);
                    logger.info(guest.getName() + " has been paid the advance, collected amount is " + amount);
                }

                logger.info("{}", "Transaction between " + guest.getName() + " and " + owner.getName()
                        + " is saved. Transaction amount is " + amount);
                rat.addFlashAttribute("success", "Amount " + amount + " successfully collected");
                return "redirect:/owner/" + guest.getPgDetails().getId() + "/" + guestId + "/"
                        + guest.getName().replace(" ", "-");
            } else {
                rat.addFlashAttribute("error", "Amount should be more than 0");
                return "redirect:/owner/" + guest.getPgDetails().getId() + "/" + guestId + "/"
                        + guest.getName().replace(" ", "-");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}", e.getMessage());
            rat.addFlashAttribute("error", "Something went wrong on server");
            return "redirect:/owner/dashboard";
        }
    }


    // Search guest by owner in guest list
    @GetMapping("/search/{query}")
    public ResponseEntity<List<Guest>> search(@PathVariable("query") String query) {
        logger.info(query);
        List<Guest> guest = guestService.getSearchedGuest(query, this.owner);
        // logger.info("Guest size is {}",guest.size());
        return ResponseEntity.ok(guest);
    }

    // Guest Allocation page view
    @GetMapping("/{pgId}/allocate/guest")
    public String guestAllocation(@PathVariable("pgId") String pgId, Model model) {
        List<Guest> unallocGuest = null;
        try {
            PgDetails pgDetails = pgService.getPgById(pgId);
            if (pgDetails.getOwner().getId().equals(owner.getId())) {
                logger.info("Owner wants to access allocation page");
                unallocGuest = guestService.getGuestsByRoom(owner, null);
                List<Floor> floors = floorService.getFloorsByPg(pgDetails);

                model.addAttribute("pg", pgDetails);
                model.addAttribute("unalloc", unallocGuest);
                model.addAttribute("floors", floors);
                logger.info("Unallocated guest count is {}",unallocGuest.size());
                return "ownerviews/guestallocation";
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "redirect:/access-denied";
    }
    
    // Allocate room of unallocated guest by owner
    /**
     * @return
     */
    @PostMapping("/{gId}/allocate/room")
    public String allocateRoom(@PathVariable("gId") String gId, @RequestParam("room") Room room,
    @RequestParam("floor") Floor floor, @RequestParam("flat") Flat flat) {
        Guest guest = guestService.getGuestById(gId); 
        try {
            guest.setFloor(floor);
            guest.setFlat(flat);
            guest.setRoom(room);
            guestService.updateGuest(guest.getId(), guest);
            return "Done";
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            return "Error";
        }
    }

    @PostMapping("/floor/{flId}")
    public ResponseEntity<List<Flat>> getFlat(@PathVariable("flId") int floorid) {
        Floor floor = null;
        List<Flat> flats = null;
        try {
            floor = floorService.getAFloor(floorid);
            flats = flatService.getFlatByFloor(floor);
            return ResponseEntity.ok().body(flats);
        } catch (Exception e) {
            logger.error("{}",e.getMessage());
            return null;
        }
    }

    // Owner setting page view
    @GetMapping("/setting")
    public String getSetting(Model model) {
        model.addAttribute("owner",this.owner);
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            logger.error(e.getMessage());
        }
        return "ownerviews/settingview";
    }
    
}
