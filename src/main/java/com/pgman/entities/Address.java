package com.pgman.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pgman.entities.pg.PgDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity; 
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ADDRESS")
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "CITY")
    private String city;

    @Column(length = 60, name = "STREET")
    private String street;

    @Column(name = "STATE")
    private String state;

    @Column(name = "PINCODE")
    private String pinCode;

    @Column(name = "COUNTRY")
    private String country;

    // @Column(name = "GUEST")
    @OneToMany(mappedBy = "address", fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    // @JsonBackReference
    private List<Guest> guest;

    // @Column(name = "PGDETAILS")
    @OneToMany(mappedBy = "address", fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private List<PgDetails> pgdetails;

}
