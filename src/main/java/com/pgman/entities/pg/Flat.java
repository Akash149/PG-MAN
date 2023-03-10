package com.pgman.entities.pg;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pgman.entities.Guest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Flat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    boolean hallSpace;

    @Column(unique = true)
    String name;

    boolean status;

    Date addedDate = new Date();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Guest> guest;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Floor floor;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "flat")
    List<Room> room;
}
