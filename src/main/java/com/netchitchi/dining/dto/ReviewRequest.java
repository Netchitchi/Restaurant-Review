package com.netchitchi.dining.dto;

import jakarta.validation.constraints.*;

public record ReviewRequest (
       @NotNull(message = "O restaurante é obrigatório.") Long restaurantId,
       @Min(1) @Max(5)Integer peanutScore,
       @Min(1) @Max(5)Integer eggScore,
       @Min(1) @Max(5)Integer dairyScore,
       @Size(max = 200, message = "O comentário não deve exceder os 200 carateres.")String commentary

){
}
