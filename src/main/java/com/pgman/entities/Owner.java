package com.pgman.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.pgman.entities.pg.PgDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "OWNERS")
@Getter
@Setter
@NoArgsConstructor
@Component

public class Owner implements Serializable {
    
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    @NotNull
    private String name;

    @Column(name = "EMAIL", unique = true)
    @NotNull
    private String email;

    @Column(name = "PASSWORD")
    @NotNull
    private String password;

    @Column(name = "PHONE", unique = true)
    @NotNull
    private String phoneNo;

    @Column(name = "ADDRESS")
    private String Address;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DOB")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;

    @Column(name = "OCCUPATION")
    @NotNull
    private String occupation;

    @Column(name = "PROFILE")
    private String profile;

    @Column(name = "ENABLED")
    private boolean enabled;

    // It's for current month
    @Column(name = "collectedRent")
    private int collectedRent;

    // It's for whole month
    @Column(name = "TOTALRENTCOLLECTED")
    private int totalRentcollected;

    @Column(name = "ROLE")
    @NotNull
    private String role;

    @Transient
    public int totalGuest = 0;

    //One owner have may be many customers;
    // @Column(name = "USER")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Guest> guest;

    //One owner have may be many pg;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.EAGER)
    // @Column(name = "PGDETAILS")
    private List<PgDetails> pgDetails;
  
    @Column(name = "REGDATE", updatable = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate regDate = LocalDate.now();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Payments> payments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Transactions> transactions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Notifications> notifications;
    
}
