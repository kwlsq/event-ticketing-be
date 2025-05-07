package com.purwafest.purwafest.auth.application.impl;

import com.purwafest.purwafest.auth.application.AuthService;
import com.purwafest.purwafest.auth.application.TokenGeneratorService;
import com.purwafest.purwafest.auth.domain.exceptions.LoginFailedException;
import com.purwafest.purwafest.auth.infrastructure.repository.BlackListTokenRepository;
import com.purwafest.purwafest.auth.presentation.dtos.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenGeneratorService tokenGeneratorService;
    private final BlackListTokenRepository blackListTokenRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenGeneratorService tokenGeneratorService, BlackListTokenRepository blackListTokenRepository){
        this.authenticationManager = authenticationManager;
        this.tokenGeneratorService = tokenGeneratorService;
        this.blackListTokenRepository = blackListTokenRepository;
    }

    @Override
    public LoginResponse credentialLogin (String email,String password){
        try{
            Authentication loginResult = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email,password)
            );
            String scope = loginResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).reduce((a, b) -> a + " " + b).orElse("");
            return LoginResponse.builder()
                    .accessToken(tokenGeneratorService.generateAccessToken(email,scope))
                    .refreshToken(tokenGeneratorService.generateRefreshToken(email))
                    .build();
        } catch (Exception e) {
            throw new LoginFailedException("Invalid email or password");
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken){
        if(tokenGeneratorService.isRefreshToken(refreshToken)){
            blackListTokenRepository.addToBlackList(refreshToken);
        } else {
            throw new IllegalArgumentException("Invalid token type");
        }
        blackListTokenRepository.addToBlackList(accessToken);
    }
}


