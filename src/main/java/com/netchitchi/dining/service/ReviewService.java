package com.netchitchi.dining.service;

import com.netchitchi.dining.dto.ReviewDecisionRequest;
import com.netchitchi.dining.dto.ReviewRequest;
import com.netchitchi.dining.dto.ReviewResponse;
import com.netchitchi.dining.model.Review;
import com.netchitchi.dining.model.Restaurant;
import com.netchitchi.dining.model.ReviewStatus;
import com.netchitchi.dining.repository.ReviewRepository;
import com.netchitchi.dining.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    public ReviewService(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<ReviewResponse> pendingReviews (){

        List<Review> review = reviewRepository.findAllByStatus(ReviewStatus.PENDING);

        return  review.stream().map(ReviewResponse::from).toList();
    }

    public ReviewResponse add(ReviewRequest reviewRequest, String reviewer){

        if (reviewer == null){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(restaurantRepository.findById(reviewRequest.restaurantId()).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O restaurante que tentou achar não existe");
        }

        Review review = new Review();
        review.setStatus(ReviewStatus.PENDING);
        review.setReviewer(reviewer);
        review.setRestaurantId(reviewRequest.restaurantId());
        review.setPeanutScore(reviewRequest.peanutScore());
        review.setEggScore(reviewRequest.eggScore());
        review.setDairyScore(reviewRequest.dairyScore());
        review.setCommentary(reviewRequest.commentary());

       Review saved= reviewRepository.save(review);

        return ReviewResponse.from(saved);
    }

    @Transactional
    public ReviewResponse setStatus(ReviewDecisionRequest reviewDecisionRequest, Long id){

        if (!reviewRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (reviewDecisionRequest == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estado é obrigatorio.");
        }

        Review review = reviewRepository.findById(id).get();

        if(reviewDecisionRequest.acceptedReview()){
            review.setStatus(ReviewStatus.ACCEPTED);
        }else{
            review.setStatus(ReviewStatus.REJECTED);
        }

        Review saved = reviewRepository.save(review);

        updateRestaurantScores(saved.getRestaurantId());
        return ReviewResponse.from(saved);

    }


    public List<ReviewResponse>  updateRestaurantScores( Long restaurantId){
        List<Review> reviewList = reviewRepository.findByRestaurantIdAndStatus(restaurantId, ReviewStatus.ACCEPTED);
        if (!restaurantRepository.existsById(restaurantId)){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Nao foi achado esse restaurante");
        }

        int totalPeanutScore = 0;
        int totalEggScore = 0;
        int totalDairyScore = 0;

        int countPeanutScore = 0;
        int countEggScore = 0;
        int countDairyScore = 0;

        for (Review review : reviewList){

            if(review.getDairyScore() != null){
                totalDairyScore += review.getDairyScore();
                countDairyScore++;
            }
            if (review.getPeanutScore() != null){
                totalPeanutScore += review.getPeanutScore();
                countPeanutScore++;
            }
            if(review.getEggScore() != null){
                totalEggScore += review.getEggScore();
                countEggScore++;
            }
        }

        Double averagePeanutScore = countPeanutScore > 0 ? (double) totalPeanutScore / countPeanutScore : null;
        Double averageEggScore = countEggScore > 0 ? (double) totalEggScore / countEggScore : null;
        Double averageDairyScore = countDairyScore > 0 ? (double) totalDairyScore / countDairyScore : null;

        double sumOfAverages = 0;
        int countOfAverages = 0;

        if (averagePeanutScore != null) { sumOfAverages += averagePeanutScore; countOfAverages++; }
        if (averageEggScore != null)    { sumOfAverages += averageEggScore;    countOfAverages++; }
        if (averageDairyScore != null)  { sumOfAverages += averageDairyScore;  countOfAverages++; }

        Double overallScore = countOfAverages > 0 ? sumOfAverages / countOfAverages : null;

        Restaurant restaurantFound = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        restaurantFound.setDairyScore(averageDairyScore);
        restaurantFound.setEggScore(averageEggScore);
        restaurantFound.setPeanutScore(averagePeanutScore);
        restaurantFound.setOverallScore(overallScore);

        restaurantRepository.save(restaurantFound);
        
        return reviewList.stream().map(ReviewResponse::from).toList();



    }



}
