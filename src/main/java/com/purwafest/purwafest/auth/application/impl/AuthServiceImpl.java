package com.purwafest.purwafest.auth.application.impl;

import com.purwafest.purwafest.auth.application.AuthService;
import com.purwafest.purwafest.auth.application.TokenGeneratorService;
import com.purwafest.purwafest.auth.domain.exceptions.LoginFailedException;
import com.purwafest.purwafest.auth.presentation.dtos.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenGeneratorService tokenGeneratorService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenGeneratorService tokenGeneratorService){
        this.authenticationManager = authenticationManager;
        this.tokenGeneratorService = tokenGeneratorService;
    }

    @Override
    public LoginResponse credentialLogin (String email,String password){
        try{
            Authentication loginResult = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email,password)
            );
            return LoginResponse.builder()
                    .accessToken(tokenGeneratorService.generateAccessToken(email,loginResult))
                    .refreshToken(tokenGeneratorService.generateRefreshToken(email,loginResult))
                    .build();
        } catch (Exception e) {
            throw new LoginFailedException("Invalid email or password");
        }
    }
}


