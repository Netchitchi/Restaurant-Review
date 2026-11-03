package com.netchitchi.dining.dto;

import com.netchitchi.dining.model.User;
import com.netchitchi.dining.model.UserRole;

public record UserResponse(Long id, String name, UserRole role, String city, String zipCode, String state,
                           Boolean peanutAllergiesInterest, Boolean eggAllergiesInterest,
                           Boolean dairyAllergiesInterest) {

    public static UserResponse from (User u){
        return new UserResponse(u.getId(), u.getName(), u.getRole(), u.getCity(), u.getZipCode(), u.getState(), u.getPeanutAllergiesInterest(), u.getEggAllergiesInterest(), u.getDairyAllergiesInterest());
    }


}



