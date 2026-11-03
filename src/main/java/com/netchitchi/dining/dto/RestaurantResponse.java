package com.netchitchi.dining.dto;

import com.netchitchi.dining.model.Restaurant;

public record RestaurantResponse(Long id, String name, String city, String state, String zipCode, Double eggScore,
                                 Double peanutScore, Double dairyScore, Double overallScore) {

    public static RestaurantResponse from (Restaurant restaurant){

        return new RestaurantResponse(restaurant.getId(), restaurant.getName(), restaurant.getCity(), restaurant.getState(), restaurant.getZipCode(), restaurant.getEggScore(), restaurant.getPeanutScore(), restaurant.getDairyScore(), restaurant.getOverallScore());
    }
}
