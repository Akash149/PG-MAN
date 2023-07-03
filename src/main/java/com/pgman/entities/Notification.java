package com.pgman.entities;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @NotNull
    private String message;

    private boolean isRead;

    @CreationTimestamp
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Notifications> notifications;
    
}
