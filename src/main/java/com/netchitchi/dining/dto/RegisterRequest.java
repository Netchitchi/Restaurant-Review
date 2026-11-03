package com.netchitchi.dining.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.netchitchi.dining.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = " O nome e obrigatorio") @Size(min = 3, max = 40, message = "insira apenas o primeiro e o ultimo nome") String name,
        @NotBlank @Size(min = 8, max = 20, message = "a password deve ter entre 8 a 20 carateres") String password,
        String city,
        String zipCode,
        String state,
        Boolean peanutAllergiesInterest,
        Boolean eggAllergiesInterest,
        Boolean dairyAllergiesInterest) {



}

