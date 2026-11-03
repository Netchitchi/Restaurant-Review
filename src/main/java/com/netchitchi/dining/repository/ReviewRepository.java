package com.netchitchi.dining.repository;

import com.netchitchi.dining.model.Review;
import com.netchitchi.dining.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional <Review> findByReviewer(String Reviewer);
    List<Review> findAllByStatus(ReviewStatus status);

    List<Review> findByRestaurantIdAndStatus(Long restaurantId, ReviewStatus status);

}
