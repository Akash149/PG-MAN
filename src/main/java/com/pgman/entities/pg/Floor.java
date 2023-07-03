package com.pgman.entities.pg;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
public class Floor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String name;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate addedDate = LocalDate.now();

    @Column(columnDefinition = "boolean default true")
    private boolean status;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "floor", fetch = FetchType.EAGER)
    private List<Guest> guest;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JsonIgnore
    private PgDetails pgDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "floor")
    @JsonIgnore
    private List<Flat> flat;

}
