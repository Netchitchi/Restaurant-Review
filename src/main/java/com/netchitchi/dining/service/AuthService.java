package com.netchitchi.dining.service;

import com.netchitchi.dining.dto.LoginRequest;
import com.netchitchi.dining.dto.RegisterRequest;
import com.netchitchi.dining.dto.TokenResponse;
import com.netchitchi.dining.dto.UserResponse;
import com.netchitchi.dining.model.User;
import com.netchitchi.dining.repository.UserRepository;
import com.netchitchi.dining.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public UserResponse registerUser( RegisterRequest registerRequest){
        if(userRepository.findByName(registerRequest.name()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este nome já está em uso, escolha outro");
        }
        User user = new User();
        user.setName(registerRequest.name());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setCity(registerRequest.city());
        user.setState(registerRequest.state());
        user.setZipCode(registerRequest.zipCode());
        user.setPeanutAllergiesInterest(registerRequest.peanutAllergiesInterest());
        user.setEggAllergiesInterest(registerRequest.eggAllergiesInterest());
        user.setDairyAllergiesInterest(registerRequest.dairyAllergiesInterest());
        userRepository.save(user);

        return UserResponse.from(user);
    }

//    public TokenResponse login(LoginRequest loginRequest){
//      User user = userRepository.findByName(loginRequest.name())
//        .filter( u -> passwordEncoder.matches(loginRequest.password(), u.getPassword()))
//        .orElseThrow( () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nome ou password inválidos.") );
//
//        return new TokenResponse(tokenService.generateToken(user), UserResponse.from(user));
//    }

    public TokenResponse login(LoginRequest loginRequest){
        Optional<User> userOptional = userRepository.findByName(loginRequest.name());
        if (userOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nome ou password inválidos.");
        }
        User userFound = userOptional.get();
        if(!passwordEncoder.matches(loginRequest.password(), userFound.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nome ou password inválidos.");
        }
        return new TokenResponse(tokenService.generateToken(userFound), UserResponse.from(userFound));
    }
}
