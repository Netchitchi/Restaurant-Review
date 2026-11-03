package com.netchitchi.dining.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DINING_REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REVIEWER")
    private String reviewer;

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ReviewStatus status;

    @Min(1)
    @Max(5)
    @Column(name = "PEANUT_SCORE")
    private Integer peanutScore;

    @Min(1)
    @Max(5)
    @Column(name = "EGG_SCORE")
    private Integer eggScore;

    @Min(1)
    @Max(5)
    @Column(name = "DAIRY_SCORE")
    private Integer dairyScore;

    @Size( max = 200, message = "O comentário não deve exceder os 200 carateres")
    @Column(name = "COMMENTARY")
    private String commentary;


}
