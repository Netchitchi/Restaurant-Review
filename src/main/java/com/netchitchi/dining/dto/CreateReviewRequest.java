package com.netchitchi.dining.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(@NotNull Long restaurantId,
                                  @Size(min = 1, max = 5) Integer peanutScore,
                                  @Size(min = 1, max = 5) Integer eggScore,
                                  @Size(min = 1, max = 5) Integer dairyScore,
                                  @Size(min = 3, max = 200, message = "O comentário não deve exceder os 200 carateres") String commentary) {
}
