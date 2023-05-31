package com.pgman.entities.pg;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pgman.entities.Address;
import com.pgman.entities.Guest;
import com.pgman.entities.Owner;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PGDETAILS")
@Getter
@Setter
@NoArgsConstructor
public class PgDetails implements Serializable{
    
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TOTALFLOOR")
    private int totalfloor;

    @Column(name = "TOTALROOM")
    private int totalRoom;

    @Column(name = "REGDATE", updatable = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate regDate = LocalDate.now();

    // @Column(name = "ADDRESS")
    @ManyToOne
    @JsonIgnore
    private Address address;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @Column(name = "OWNER")
    @JsonIgnore
    private Owner owner;

    @Column(name = "PROFILE")
    private String profile;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Policy policy;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "pgDetails")
    // @Column(name = "GUEST")
    private List<Guest> guest; 
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "pgDetails")
    private List<Floor> floor;
    
}
