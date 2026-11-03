package com.netchitchi.dining.controller;

import com.netchitchi.dining.dto.ReviewDecisionRequest;
import com.netchitchi.dining.dto.ReviewRequest;
import com.netchitchi.dining.dto.ReviewResponse;
import com.netchitchi.dining.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {

        this.reviewService = reviewService;
    }

    @PostMapping("/addReview")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse submitReview(@Valid @RequestBody ReviewRequest reviewRequest, Authentication authentication){

        return reviewService.add(reviewRequest, authentication.getName());
    }

    //Admin method, must specify to the admin later
    @GetMapping("/pending")
    public List<ReviewResponse> pendingReviews(){
        return reviewService.pendingReviews();
    }

    @PutMapping("/*/decision")
    public ReviewResponse setStatus(@Valid @RequestBody ReviewDecisionRequest reviewDecisionRequest, @PathVariable("id") Long id){

       return reviewService.setStatus(reviewDecisionRequest, id);

    }

}
