package com.netchitchi.dining.service;

import com.netchitchi.dining.dto.RestaurantRequest;
import com.netchitchi.dining.dto.RestaurantResponse;
import com.netchitchi.dining.model.Restaurant;
import com.netchitchi.dining.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantResponse add(RestaurantRequest restaurantRequest){
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByZipCodeAndName(restaurantRequest.zipCode(), restaurantRequest.name());

        if (restaurantOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse restaurante já existe neste código postal.");
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantRequest.name());
        restaurant.setCity(restaurantRequest.city());
        restaurant.setState(restaurantRequest.state());
        restaurant.setZipCode(restaurantRequest.zipCode());

       Restaurant saved =  restaurantRepository.save(restaurant);

        return RestaurantResponse.from(saved);

    }

    public RestaurantResponse details(String name, String zipCode){
        Restaurant restaurant = restaurantRepository
                .findByZipCodeAndName(zipCode,name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado um restaurante com esse nome neste código postal."));

        return RestaurantResponse.from(restaurant);
    }

    public List<RestaurantResponse> findByAllergy(String zipCode, String allergy){

        List<Restaurant> restaurants;

        switch (allergy.toLowerCase()){
            case "peanut":
                restaurants = restaurantRepository.findByZipCodeAndPeanutScoreIsNotNullOrderByPeanutScoreDesc(zipCode);
                break;
            case "egg":
                restaurants = restaurantRepository.findByZipCodeAndEggScoreIsNotNullOrderByEggScoreDesc(zipCode);
                break;
            case "dairy":
                restaurants = restaurantRepository.findByZipCodeAndDairyScoreIsNotNullOrderByDairyScoreDesc(zipCode);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Alergia inválida fornecida. Use: peanut, dairy ou egg.");
        }

        if (restaurants.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "Nenhum restaurante encontrado com pontuação para esta alergia," +
                            " na localização desejada.");
        }

        return restaurants.stream()
                .map(RestaurantResponse::from)
                .toList();
    }



}
