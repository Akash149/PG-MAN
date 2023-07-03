package com.pgman.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
public class Notifications {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;

    @ManyToOne
    @JsonIgnore
    private Notification notification;

    @ManyToOne
    @JsonIgnore
    private Guest guests;

    @ManyToOne
    @JsonIgnore
    private Owner owners;
    
}
