package com.netchitchi.dining.security;

import com.netchitchi.dining.model.User;
import com.netchitchi.dining.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public JwtFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Try to get the token from the request (null if there isn't a usable one)
        String token = extractToken(request);

        // 2. Only if there is a token, try to log the user in
        if (token != null) {
            authenticateWithToken(token);
        }

        // 3. ALWAYS continue, whatever happened above.
        // If the user was not authenticated, Spring Security blocks protected URLs later.
        filterChain.doFilter(request, response);
    }

    // Reads "Authorization: Bearer <token>" and returns just the <token>.
    // Returns null when the header is missing, in the wrong format, or the token is empty.
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        String token = header.substring(7).trim();   // remove "Bearer " (7 characters)
        return token.isEmpty() ? null : token;
    }

    // Validates the token and, if everything is OK, marks the request as authenticated.
    // Each "return" below means: "something is wrong, so stay NOT logged in".
    private void authenticateWithToken(String token) {

        // Invalid, expired or tampered token -> empty
        Optional<String> userName = tokenService.validateToken(token);
        if (userName.isEmpty()) {
            return;
        }

        // The token is valid but the user no longer exists (e.g. deleted) -> empty
        Optional<User> maybeUser = userRepository.findByName(userName.get());
        if (maybeUser.isEmpty()) {
            return;
        }
        User user = maybeUser.get();

        // Build the "who is logged in" object using Spring's own UserDetails.
        // (Full name used because our own entity is also called User.)
        // roles("ADMIN") automatically adds the "ROLE_" prefix that hasRole("ADMIN") needs.
        UserDetails principal = org.springframework.security.core.userdetails.User
                .withUsername(user.getName())
                .password("")                        // not needed: the token already proved who they are
                .roles(user.getRole().name())        // USER or ADMIN, read fresh from the database
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        // Tell Spring Security: "this request is authenticated as this user"
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}