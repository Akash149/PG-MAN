package com.pgman.entities.pg;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Policy implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String message;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String rule;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate addedDate = LocalDate.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "policy")
    private List<PgDetails> pgdetails;
    
}
