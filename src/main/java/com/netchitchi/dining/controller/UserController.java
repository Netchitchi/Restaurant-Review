package com.netchitchi.dining.controller;

import com.netchitchi.dining.dto.UpdateUser;
import com.netchitchi.dining.dto.UserResponse;
import com.netchitchi.dining.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    final private UserService userService;


    public UserController( UserService userService) {
        this.userService = userService;
    }


    @PutMapping("/updateUser/me")
    public UserResponse updateUser(@AuthenticationPrincipal UserDetails currentUser, @RequestBody @Valid UpdateUser updateUser){
       return userService.updateUser(currentUser.getUsername(), updateUser);
    }

    @GetMapping("/me")
    public UserResponse getMe(Authentication authentication){

        return userService.getByName(authentication.getName());
    }

    @GetMapping("/")
    public List<UserResponse> getAll(){

        return userService.getAll();
    }

}
