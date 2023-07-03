package com.pgman.controller;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgman.dto.FlatDTO;
import com.pgman.dto.FloorDTO;
import com.pgman.dto.RoomDTO;
import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.Payments;
import com.pgman.entities.PgUtilities;
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
import com.pgman.service.PgService;
import com.pgman.service.TransactionService;
import com.pgman.service.pg.FlatService;
import com.pgman.service.pg.FloorService;
import com.pgman.service.pg.PolicyService;
import com.pgman.service.pg.RoomService;

import lombok.experimental.PackagePrivate;

@CrossOrigin(origins = { "*" }, maxAge = 4800, allowCredentials = "false")
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
    private List<PgDetails> pgs = new ArrayList<PgDetails>();
    // private List<Transactions> transactions;
    private Page<Transactions> transactions;
    private List<Guest> guests;

    /**
     * @param model
     * @param principal
     */
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

    /**
     * @param model
     * @return
     */
    @GetMapping("/dashboard")
    public String ownerNewDashboard(Model model) {
        try {
            // get recent 5 guests
            Pageable pageable = PageRequest.of(0, 5);
            Page<Guest> guests = guestService.getRecentGuest(owner, pageable);
            Pageable pagesize = PageRequest.of(0, 14);
            int percent = pgUtilities.getRentCollectionPercentage(owner);
            transactions = transactionService.getSomeTransactionByOwner(owner, pagesize);
            logger.info("Owner Transactions are loaded, count is {}", transactions.getSize());
            model.addAttribute("rguest", guests);
            model.addAttribute("title", owner.getName());
            model.addAttribute("trans", transactions);
            model.addAttribute("percentage", percent);
            model.addAttribute("totalRent", pgUtilities.getTotalRent());
            model.addAttribute("collected", pgUtilities.getCollectedAmount());
            model.addAttribute("remaining", pgUtilities.getRemainingAmount());
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
        }
        return "ownerviews/ownerdash";
    }

    // It shows the main view of pg and their details
    /**
     * @param id
     * @param name
     * @param model
     * @return
     */
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
                    logger.info("UnAllocated guest count is {}", unallocGuest.size());
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
    /**
     * @return
     */
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
    /**
     * @param pgId
     * @param guestId
     * @param guestName
     * @param model
     * @return
     */
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
    /**
     * @param policy
     * @param pgid
     * @param rat
     * @return
     */
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
    /**
     * @param policy
     * @param pgid
     * @param rat
     * @return
     */
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
    /**
     * @param id
     * @param model
     * @return
     */
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
    /**
     * @param floor
     * @param id
     * @param model
     * @param rat
     * @return
     */
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
    /**
     * @param fid
     * @param rat
     * @return
     */
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
    /**
     * @param pgId
     * @param guestId
     * @param rat
     * @return
     */
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
    /**
     * @param pgId
     * @param guestId
     * @param rat
     * @return
     */
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
    /**
     * @param guestId
     * @param amount
     * @param collType
     * @param rat
     * @return
     */
    @PostMapping("/collect-cash")
    public String collectCash(@RequestParam("guestId") String guestId,
            @RequestParam("amount") int amount, @RequestParam("collectiontype") String collType,
            RedirectAttributes rat) {
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

                if (collType.equals("rent")) {
                    transactions.setType("rent");
                    transactionService.updateTransaction(transactions.getId(), transactions);
                    guest.setPaidAmount(amount);
                    guest.setRemainingAmount(guest.getRemainingAmount() - amount);
                    guest.setPaymentStatus(true);
                    guestService.updateGuest(guestId, guest);
                    logger.info(guest.getName() + " has been paid their rent, collected amount is " + amount);
                }

                if (collType.equals("advance")) {
                    transactions.setType("advance");
                    transactionService.updateTransaction(transactions.getId(), transactions);
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
    /**
     * @param query
     * @return
     */
    @GetMapping("/search/{query}")
    public ResponseEntity<List<Guest>> search(@PathVariable("query") String query) {
        logger.info(query);
        List<Guest> guest = guestService.getSearchedGuest(query, this.owner);
        // logger.info("Guest size is {}",guest.size());
        return ResponseEntity.ok(guest);
    }

    // Guest Allocation page view
    /**
     * @param pgId
     * @param model
     * @return
     */
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
                logger.info("Unallocated guest count is {}", unallocGuest.size());
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
    @PostMapping("/{guestId}/allocate/room")
    public String allocateRoom(@PathVariable("gId") String gId, @RequestParam("room") int roomId,
            @RequestParam("floor") int floorId, @RequestParam("flat") int flatId) {
        Guest guest = guestService.getGuestById(gId);
        try {
            Floor floor = floorService.getAFloor(floorId);
            Flat flat = flatService.getAFlat(flatId);
            Room room = roomService.getARoom(roomId);
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

    // Bed allotment of a gurest by owner
    // @PostMapping("/allocate/room/{guestId}/{floorId}/{flatId}/{roomId}")
    @PutMapping("/allocate/room/guest")
    public ResponseEntity<String> allocateRoomOfGuest(@RequestParam("guestId") String guestId,
            @RequestParam("floorId") int floorId, @RequestParam("flatId") int flatId,
            @RequestParam("roomId") int roomId) {
        logger.info("Allocation handler start");
        try {
            Guest guest = guestService.getGuestById(guestId);
            Floor floor = floorService.getAFloor(floorId);
            Flat flat = null;
            Room room = null;
            // var flat = floor.getFlat().stream().filter(flt -> flt.getId() == flatId);
            for (Flat flt : floor.getFlat()) {
                if (flt.getId() == flatId)
                    flat = flt;

                for (Room rm : flt.getRoom()) {
                    if (rm.getId() == roomId)
                        room = rm;
                }
            }
            guest.setFloor(floor);
            guest.setFlat(flat);
            guest.setRoom(room);
            guestService.updateGuest(guestId, guest);
            logger.info("{}", guest.getName() + " allocated in floor: " + floor.getName() + ", flat: " + flat.getName()
                    + ", room: " + room.getName());
            return ResponseEntity.ok("done");
        } catch (Exception e) {
            ResponseEntity.ok("error");
            logger.error("{}", e.getMessage());
            return ResponseEntity.status(500).build();
        }

    }

    /**
     * @param floorid
     * @return
     */
    @GetMapping("/floor/{flId}")
    public ResponseEntity<List<FlatDTO>> getFlat(@PathVariable("flId") int floorid) {
        Floor floor = null;
        // FlatDTO flatdto = new FlatDTO();
        List<Flat> flats = null;
        List<FlatDTO> flatDto = new ArrayList<FlatDTO>();
        try {
            floor = floorService.getAFloor(floorid);
            // flats = flatService.getFlatByFloor(floor);
            // return ResponseEntity.ok().body(flats);
            if (floor.getPgDetails().getOwner().getId().equals(this.owner.getId())) {
                flats = flatService.getFlatByFloor(floor);
                for (Flat flt : flats) {
                    FlatDTO flatdto = new FlatDTO(flt.getId(), flt.getName());
                    flatDto.add(flatdto);
                }
                logger.info("Total count of Flat is {}", flats.size());
                return ResponseEntity.ok().body(flatDto);
            } else {
                logger.warn("UnAuthorized Access, Permission denied");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            return null;
        }
    }

    // Get room by flat
    /**
     * @param flatId
     * @return
     */
    @GetMapping("/flat/{flatId}")
    public ResponseEntity<List<RoomDTO>> getRoom(@PathVariable("flatId") String flatId) {
        List<Room> roomlist = new ArrayList<Room>();
        List<RoomDTO> roomDto = new ArrayList<RoomDTO>();
        Flat flat = null;
        try {
            flat = flatService.getAFlat(Integer.parseInt(flatId));
            if (flat != null) {
                roomlist = flat.getRoom();
                for (Room room : roomlist) {
                    RoomDTO rDto = new RoomDTO(room.getId(), room.getName());
                    roomDto.add(rDto);
                }
                logger.info("Total count of Room is {}", roomlist.size());
            } else {
                logger.info("Flat is Null or not found flat");
            }

        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            return null;
        }
        return ResponseEntity.ok().body(roomDto);
    }

    // Owner setting page view
    /**
     * @param model
     * @return
     */
    @GetMapping("/setting")
    public String getSetting(Model model) {
        model.addAttribute("owner", this.owner);
        try {

        } catch (Exception e) {
            // TODO: handle exception
            // e.printStackTrace();
            logger.error(e.getMessage());
        }
        return "ownerviews/settingview";
    }

    // Owner can add a PG (pageview)
    @GetMapping("/add/pg")
    public String addAPg(Model model) {
        model.addAttribute("owner", this.owner);
        return "ownerviews/addpgview";
    }

    /**
     * @param pgName
     * @return
     */
    @PostMapping("/add/new/pg")
    public ResponseEntity<String> addPg(@RequestParam("name") String pgName) {
        PgDetails pg = new PgDetails();
        ResponseEntity response = null;
        try {
            int num = (int)(Math.random()*9000)+1000;
            //String[] fname = pgName.split(" ");
            // String id = (String) num + fname[0].getChars(0, 2, null, num);
            pg.setName(pgName);
            pg.setId(String.valueOf(num));
            pg.setOwner(this.owner);
            pgService.addPg(pg);

            response = ResponseEntity.ok().body("done");
        } catch (Exception e) {
            logger.error("{}",e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    // Get Flat by their id
    @GetMapping("/pg/flat/{flatId}")
    public ResponseEntity<Flat> getFlatDetails(@PathVariable("flatId") int flatId) {
        ResponseEntity response = null;
        Flat flat = null;
        try {
            flat = flatService.getAFlat(flatId);
            if (flat != null) {
                flat.setGuest(null);
                flat.setRoom(null);
                flat.setFloor(null);
                logger.info("Sending " + flat.getId() + ", " + flat.getName());
                response = ResponseEntity.ok().body(flat);
            } else {
                response = ResponseEntity.ok().body(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    // Add new flat in a floor
    @PostMapping("/pg/add/flat")
    public ResponseEntity<String> addAFlat(Flat newFlat) {
        ResponseEntity response = null;
        try {
            flatService.addFlat(newFlat);
            logger.info("New flats are added");
            response = ResponseEntity.ok().body("done");
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    // update flat by their id
    @PutMapping("/pg/update/flat/{flatId}")
    public ResponseEntity<String> updateFlat(@PathVariable("flatId") int flatId, Flat flat) {
        ResponseEntity response = null;
        Flat flat_ = null;
        try {
            flat_ = flatService.getAFlat(flatId);
            if(flat_ != null) {
                flat_ = flat;
                flatService.updateFlat(flatId, flat_);
                logger.info("{}", flat.getName() + " has been updated");
                response = ResponseEntity.ok("done");
            } else {
                logger.warn("{}",flatId + " not found in database");
                response = ResponseEntity.ok().body(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    // Get a room by their id
    @GetMapping("/pg/room/{roomId}")
    public ResponseEntity<Room> getARoom(@PathVariable("roomId") int roomId) {
        Room room = null;
        ResponseEntity response = null;
        try {
            room = roomService.getARoom(roomId);
            if (room != null) {
                room.setGuest(null);
                room.setFlat(null);
                logger.info("Sending " + room.getId() + ", " + room.getName());
                response = ResponseEntity.ok().body(room);
            } else {
                response = ResponseEntity.ok().body(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    // add room
    @PostMapping("/pg/add/room")
    public ResponseEntity<String> addRoom(Room room) {
        ResponseEntity response = null;
        try {
            roomService.addRoom(room);
            logger.info("{}", room.getName() + " is added in " + room.getFlat().getName());
            response = ResponseEntity.ok("done");
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    // Update room
    /**
     * @param roomId
     * @param room
     * @return
     */
    @PutMapping("/pg/update/room/{roomId}")
    public ResponseEntity<String> updateRoom(@PathVariable("roomId") int roomId, Room room) {
        ResponseEntity response = null;
        try {
            roomService.updateRoom(roomId,room);
            logger.info("{}", room.getName() + " has been updated ");
            response = ResponseEntity.ok("done");   
            
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    // Get a floor by their id
    @GetMapping("/pg/floor/{id}")
    public ResponseEntity<FloorDTO> getFloor(@PathVariable("id") int id) {
        ResponseEntity response = null;
        FloorDTO floorDTO = new FloorDTO();
        try {
            Floor floor = floorService.getAFloor(id);
            
            if (floor != null) {
                floorDTO.setAddedDate(floor.getAddedDate());
                floorDTO.setName(floor.getName());
                floorDTO.setStatus(floor.isStatus());
                response = ResponseEntity.ok(floorDTO);
                logger.info("Sending " + floor.getName() + " floor data" );
            } else {
                logger.warn("{}", id + " not found in Database");
                response = ResponseEntity.ok().body(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    // Update foor
    @PutMapping("/pg/update/floor/{floorId}")
    public ResponseEntity<String> updateFloor(@PathVariable("floorId") int floorId, Floor floor) {
        ResponseEntity response = null;
        Floor floor_ = null;
        try {
            floor_ = floorService.getAFloor(floorId);
            if (floor_ != null) {
                floor.setPgDetails(floor_.getPgDetails());
                floor_ = floor;
                floorService.updateFloor(floorId, floor_);
                logger.info("{}", floor.getName() + " has been updated");
                response = ResponseEntity.ok("done");
            } else {
                logger.warn("{}", floorId + " not found in Database");
                response = ResponseEntity.ok().body(HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    // Delete floor by their id
    @DeleteMapping("/pg/delete/floor")
    public ResponseEntity<String> deleteFloorById(@RequestParam("floorId") int floorId) {
        ResponseEntity response = null;
        try {
            floorService.deleteFloor(floorId);
            response = ResponseEntity.ok("done");
            logger.info("Florr {}",floorId + " has been deleted");
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }


}
