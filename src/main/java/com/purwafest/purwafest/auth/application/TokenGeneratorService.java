package com.purwafest.purwafest.auth.application;

import com.purwafest.purwafest.auth.domain.valueObject.Token;
import org.springframework.security.core.Authentication;

public interface TokenGeneratorService {
    Token generateAccessToken(String email, Authentication auth);
    Token generateRefreshToken(String email, Authentication auth);
}
