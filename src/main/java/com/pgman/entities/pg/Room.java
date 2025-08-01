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
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(unique = true)
    private String name;

    int bedCount;

    private String washRoomType;

    private boolean status;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate addedDate = LocalDate.now();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "room", fetch = FetchType.EAGER)
    private List<Guest> guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Flat flat;

}
