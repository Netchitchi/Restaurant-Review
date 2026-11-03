package com.netchitchi.dining.repository;

import com.netchitchi.dining.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByZipCodeAndName(String zipCode, String name);
    List<Restaurant> findByZipCodeAndDairyScoreIsNotNullOrderByDairyScoreDesc(String zipCode);
    List<Restaurant> findByZipCodeAndPeanutScoreIsNotNullOrderByPeanutScoreDesc(String zipCode);
    List<Restaurant> findByZipCodeAndEggScoreIsNotNullOrderByEggScoreDesc(String zipCode);
}
