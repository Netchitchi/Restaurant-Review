package com.netchitchi.dining.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "RESTAURANT", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "ZIP_CODE"}))
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CITY")
    private String city;

    @Column(name ="STATE")
    private String state;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column( name = "EGG_SCORE")
    private Double eggScore;

    @Column(name = "PEANUT_SCORE")
    private Double peanutScore;

    @Column(name = "DAIRY_SCORE")
    private Double dairyScore;

    @Column(name = "OVERALL_SCORE")
    private Double overallScore;


}
