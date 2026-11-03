package com.netchitchi.dining.controller;

import com.netchitchi.dining.dto.RestaurantRequest;
import com.netchitchi.dining.dto.RestaurantResponse;
import com.netchitchi.dining.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController( RestaurantService restaurantService) {

        this.restaurantService = restaurantService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse add(@Valid @RequestBody RestaurantRequest restaurantRequest){

        return restaurantService.add(restaurantRequest);
    }

    @GetMapping("/details")
    public RestaurantResponse details(@RequestParam("name") String name,@RequestParam("zipCode") String zipCode){

        return  restaurantService.details(name, zipCode);

    }

    @GetMapping("/search")
    public List<RestaurantResponse> findByAllergy( @RequestParam("zipCode") String zipCode,
                                           @RequestParam("allergy") String allergy){

        return restaurantService.findByAllergy(zipCode, allergy);

    }
}
