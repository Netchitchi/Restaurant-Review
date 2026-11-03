package com.netchitchi.dining.dto;

import com.netchitchi.dining.model.Review;
import com.netchitchi.dining.model.ReviewStatus;

public record ReviewResponse(Long id, String reviewer, Long restaurantId, ReviewStatus status, Integer peanutScore,
                             Integer eggScore, Integer dairyScore, String commentary) {

    public static ReviewResponse from(Review review){
        return new ReviewResponse(review.getId(), review.getReviewer(), review.getRestaurantId(), review.getStatus(), review.getPeanutScore(), review.getEggScore(), review.getDairyScore(), review.getCommentary());
    }
}
