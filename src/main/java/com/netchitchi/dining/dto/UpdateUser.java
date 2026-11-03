package com.netchitchi.dining.dto;

public record UpdateUser(String city, String state, String zipCode,
                         Boolean peanutAllergiesInterest, Boolean eggAllergiesInterest, Boolean dairyAllergiesInterest) {
}
