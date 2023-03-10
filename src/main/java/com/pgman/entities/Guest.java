package com.pgman.entities;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.pgman.entities.pg.Flat;
import com.pgman.entities.pg.Floor;
import com.pgman.entities.pg.PgDetails;
import com.pgman.entities.pg.Room;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Entity
@Table(name="GUEST")
@Component
@Getter
@Setter
@NoArgsConstructor
public class Guest {
    
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    @NotNull
    private String name;

    @Column(unique = true, name = "EMAIL")
    @NotNull
    private String email;

    @Column(name = "PASSWORD")
    @NotNull
    private String password;

    @Column(unique = true, name = "PHONE")
    @NotNull
    private String phone;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "OCCUPATION")
    @NotNull
    private String occupation;

    @Column(name = "DOB")
    private Date dob;

    @Column(name = "ENABLED")
    private boolean enabled;

    @Column(name = "DCUMENT")
    private String document;

    @Column(name = "PAYMENTSTATUS")
    private String paymentStatus;

    @Column(name = "REGDATE", updatable = false)
    @NotNull
    private Date regDate = new Date();

    // @Column(name = "OWNER")
    @JoinColumn(name = "OWNER")
    @ManyToOne
    private Owner owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Floor floor;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Room room;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Flat flat;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    @Column(length = 60, name = "PROFILE")
    private String profile;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @JsonIgnore
    private PgDetails pgDetails;

    @Column(name = "ROLE")
    @NotNull
    private String role;

    // @Column(name = "PAYMENTS")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guest", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Payments> payments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guest", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Transactions> transactions;
    
}
