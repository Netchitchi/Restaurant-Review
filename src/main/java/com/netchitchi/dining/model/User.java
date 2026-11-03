package com.netchitchi.dining.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "NAME", unique = true)
    private String name;

    @Column(name = "PASSWORD")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private UserRole role = UserRole.USER;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "PEANUT_ALLERGIES_INTEREST")
    private Boolean peanutAllergiesInterest;

    @Column(name = "EGG_ALLERGIES_INTEREST")
    private Boolean eggAllergiesInterest;

    @Column(name = "DAIRY_ALLERGIES_INTEREST")
    private Boolean dairyAllergiesInterest;

}
