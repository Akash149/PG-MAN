package com.pgman.entities;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Component
@Getter
@Setter
@NoArgsConstructor
// @Entity
// @Table(name = "USERS")
public class User {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    private String occupation;

    @OneToOne
    private Guest guest;

    private String gender;

    @OneToOne
    private Owner owner;

    private String role;

    
}
