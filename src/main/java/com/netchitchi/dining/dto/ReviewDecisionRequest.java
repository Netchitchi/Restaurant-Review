package com.netchitchi.dining.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public record ReviewDecisionRequest(@NotNull(message = " O estado e obrigatorio") Boolean acceptedReview) {

}
