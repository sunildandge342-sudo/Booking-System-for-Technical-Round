package com.team.booking_system.service;

import com.team.booking_system.dto.Request.LoginRequest;
import com.team.booking_system.dto.Response.AuthResponse;
import com.team.booking_system.entity.User;
import com.team.booking_system.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}