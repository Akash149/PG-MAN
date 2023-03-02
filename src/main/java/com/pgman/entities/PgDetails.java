package com.pgman.entities;

import java.util.Date;
import java.util.List;

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
public class PgDetails {
    
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
    private Date regDate = new Date();

    // @Column(name = "ADDRESS")
    @ManyToOne
    private Address address;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @Column(name = "OWNER")
    private Owner owner;

    @Column(name = "PROFILE")
    private String profile;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pgDetails")
    // @Column(name = "GUEST")
    private List<Guest> guest;   
    
}
