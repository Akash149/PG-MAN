package com.pgman.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PAYMENTS")
public class Payments {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "REFNO")
    private Long refNo;

    @Column(name = "GATEWAY")
    private String gateway;

    @Column(name = "AMOUNT")
    private float amount;

    @Column(name = "DATE")
    private Date date;
    
    @ManyToOne
    @JsonIgnore
    private Guest guest;

    @ManyToOne
    @JsonIgnore
    private Owner owner;

    @OneToMany
    private List<Transactions> transactions;

}
