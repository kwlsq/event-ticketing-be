package com.purwafest.purwafest.auth.application;

import com.purwafest.purwafest.auth.domain.valueObject.Token;
import org.springframework.security.core.Authentication;

public interface TokenGeneratorService {
    Token generateAccessToken(String email, String scopes);
    Token generateAccessToken(String refreshToken);
    Token generateRefreshToken(String email);
    boolean isRefreshToken(String token);
}
