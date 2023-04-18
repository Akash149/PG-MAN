package com.pgman.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.entities.User;
import com.pgman.entities.pg.PgDetails;
import com.pgman.service.PgService;
import com.pgman.helper.DateConvreter;
import com.pgman.service.GuestService;
import com.pgman.service.OwnerService;

@Controller
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private Guest guest;

    @Autowired
    private GuestService guestService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private PgService pgService;

    private List<String> pgNames = new ArrayList<String>();

    // @Autowired
    // private User user;

    @Autowired
    private Owner owner;

    //Get common data
    @ModelAttribute
    public void getCommon(Model model, Principal principal) {
        
        if (principal != null) {
            String name = principal.getName();
            if(name.isEmpty()) {
                logger.info("No users are logged in");
            } else {
                logger.info("User {}", name + "is logged in");
                Guest guest = guestService.getGuestByEmail(name);
                Owner owner = ownerService.getOwnerByEmail(name);

                if (guest != null) {
                    model.addAttribute("guest",guest);
                }

                if (owner != null) {
                    model.addAttribute("owner", owner);
                }
            }
        }
        
        pgNames.clear();
        List<PgDetails> pgDetails = pgService.getAllPg();
        logger.info("PGs are loaded count is "+pgDetails.size());
        for(PgDetails pg: pgDetails) {
            pgNames.add(pg.getId() + " - " + pg.getName());
        }
        logger.info("PGs are "+pgNames.toString());

        model.addAttribute("pgs",pgNames);
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        
        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "SignUp");
        return "signup";
    }

    @GetMapping("/signin")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        return "login";
        // return "signin";
    }

    @GetMapping("/user")
    public String uname(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        System.out.println(auth.getPrincipal());
        System.out.println(auth.getDetails());
        model.addAttribute("username", name);
        return "Hello "+name;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute() User user, Model model, RedirectAttributes rat,
    @RequestParam("db") String db, @RequestParam("role") String role, @RequestParam("pgname") String pgname) {
        logger.info("User is " + user.toString());
        if (user.getRole() != null) {

            Date date = DateConvreter.convert(db);
            // user.setDob(date);
            
            if ("GUEST".equals(user.getRole())) {
                if (guestService.isExistByEmail(user.getEmail()) ||
                        guestService.isExistByPhone(user.getPhone())) {
                    rat.addFlashAttribute("error", "User is already registerd !!");
                    logger.warn("User "+user.getEmail()+" is already registered");
                    return "redirect:/signup";
                } else {
                    String id =pgname.substring(0,3);
                    logger.info("PG id is "+id);
                    PgDetails pgdetails = pgService.getPgById(id);
                    logger.info("Guest is "+user.getName());
                    guest.setName(user.getName());
                    guest.setEmail(user.getEmail());
                    guest.setPassword(passwordEncoder.encode(user.getPassword()));
                    guest.setGender(user.getGender());
                    guest.setPhone(user.getPhone());
                    guest.setRole(user.getRole());
                    guest.setOccupation(user.getOccupation());
                    guest.setDob(date);
                    // guest.setEnabled(true);
                    guest.setPgDetails(pgdetails);
                    guest.setOwner(pgdetails.getOwner());
                    try {
                        this.guestService.createGuest(guest);
                        rat.addFlashAttribute("success", "Successfully registered !!");
                        logger.info("Guest has been saved.");
                        return "redirect:/signup";
                    } catch (Exception e) {
                        e.printStackTrace();
                        rat.addFlashAttribute("error", "Something went Wrong on Server !!");
                        logger.error(e.getMessage());
                        return "redirect:/signup";
                    }
                }

            } else {
                // Owner owner = (Owner) user;
                if (ownerService.isExistByEmail(user.getEmail()) ||
                        ownerService.isExistByPhone(user.getPhone())) {
                    rat.addFlashAttribute("error", "User is already registerd !!");
                    logger.warn("User is already registered");
                    return "redirect:/signup";
                } else {
                    owner.setName(user.getName());
                    owner.setEmail(user.getEmail());
                    owner.setPassword(passwordEncoder.encode(user.getPassword()));
                    owner.setGender(user.getGender());
                    owner.setPhoneNo(user.getPhone());
                    owner.setRole(user.getRole());
                    owner.setOccupation(user.getOccupation());
                    owner.setDob(date);

                    try {
                        this.ownerService.creatOwner(owner);
                        rat.addFlashAttribute("success", "Successfully registered !!");
                        logger.info("Ower is successfully registered");
                        return "redirect:/signup";
                    } catch (Exception e) {
                        e.printStackTrace();
                        rat.addFlashAttribute("error", "Something went Wrong on Server !!");
                        logger.error(e.getMessage());
                        return "redirect:/signup";
                    }
                }  
            }
        }
        rat.addFlashAttribute("error","Something went wrong on server !!");
        logger.error("Something went wrong !!");
        return "redirect:/signup";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        logger.warn("Unauthorised access: Access denied !!");
        return "accessdenied";
    }

    @GetMapping("/error-404-not-found")
    public String err404(Model model) {
        model.addAttribute("title","Not found");
        logger.warn("Resource Not found");
        return "error404";
    }

    // // Admin's Views
    // // @PreAuthorize("hasRole('ADMIN')")
    // @GetMapping("/home/admin")
    // public String adminViews() {
    //     return "adminviews/adminpage";
    // }

    // // Guest's Views
    // @GetMapping("/guest")
    // public String guest() {
    //     return "guestviews/guestpage";
    // }

    // // PgOwner's Views
    // @GetMapping("/home/owner")
    // public String pgOwnerView() {
    //     return "pgownerviews/pgOwnerpage";
    // }
}
