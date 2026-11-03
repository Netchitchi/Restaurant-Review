package com.netchitchi.dining.controller;

import com.netchitchi.dining.dto.LoginRequest;
import com.netchitchi.dining.dto.RegisterRequest;
import com.netchitchi.dining.dto.TokenResponse;
import com.netchitchi.dining.dto.UserResponse;
import com.netchitchi.dining.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request){
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest){

        return authService.login(loginRequest);

    }
}
