package com.netchitchi.dining.service;

import com.netchitchi.dining.dto.UpdateUser;
import com.netchitchi.dining.dto.UserResponse;
import com.netchitchi.dining.model.User;
import com.netchitchi.dining.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getByName(String name){
        Optional<User> userOptional = userRepository.findByName(name);
        if(!userOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilizador não encontrado.");
        }
        User foundUser = userOptional.get();
        return UserResponse.from(foundUser);
    }


    public List<UserResponse> getAll(){

        return userRepository.findAll().stream().map(user -> UserResponse.from(user)).toList();
    }

    @Transactional
    public UserResponse updateUser (String name, UpdateUser updateUser){

        Optional<User> userOptional = userRepository.findByName(name);
        if (userOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User userFound = userOptional.get();

        if (updateUser.city() != null){
            userFound.setCity(updateUser.city());
        }
        if (updateUser.state() != null){
            userFound.setState(updateUser.state());
        }
        if (updateUser.zipCode() != null){
            userFound.setZipCode(updateUser.zipCode());
        }
        if (updateUser.peanutAllergiesInterest() != null){
            userFound.setPeanutAllergiesInterest(updateUser.peanutAllergiesInterest());
        }
        if (updateUser.eggAllergiesInterest() != null){
            userFound.setEggAllergiesInterest(updateUser.eggAllergiesInterest());
        }
        if (updateUser.dairyAllergiesInterest() != null){
            userFound.setDairyAllergiesInterest(updateUser.dairyAllergiesInterest());
        }

        return UserResponse.from(userFound);


    }
}
