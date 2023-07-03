package com.pgman.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Guest implements Serializable{
    
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
    private LocalDate dob;

    @Column(name = "ENABLED")
    private boolean enabled;

    @Column(name = "DCUMENT")
    private String document;

    @Column(name = "PAYMENTSTATUS")
    private boolean paymentStatus;

    @Column(name = "REGDATE", updatable = false)
    @NotNull
    private LocalDate regDate = LocalDate.now();

    @Column(name = "SHIFTDATE", updatable = true)
    // @ColumnDefault(value = "CURRENT_TIMESTAMP";
    @CreationTimestamp
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate shiftDate = LocalDate.now();

    @Column(name = "RENTAMOUNT", updatable = true, nullable = false, columnDefinition = "int default 00")
    private int rentAmount;

    @Column(name = "PAIDAMOUNT", updatable = true, nullable = false, columnDefinition = "int default 00")
    private int paidAmount;

    @Column(name = "REMAININGAMOUNT", updatable = true, nullable = false, columnDefinition = "int default 00")
    private int remainingAmount;

    @Column(columnDefinition = "int default 00")
    private int advancePaid;

    // @Column(name = "OWNER")
    @JoinColumn(name = "OWNER")
    @ManyToOne
    @JsonIgnore
    private Owner owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Floor floor;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Room room;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Flat flat;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    @Column(length = 60, name = "PROFILE")
    private String profile;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private PgDetails pgDetails;

    @Column(name = "ROLE")
    @NotNull
    private String role;

    // @Column(name = "PAYMENTS")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guest", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Payments> payments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guest", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Transactions> transactions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Notifications> notifications;
    
}
