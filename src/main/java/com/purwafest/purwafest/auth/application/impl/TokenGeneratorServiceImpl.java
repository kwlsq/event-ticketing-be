package com.purwafest.purwafest.auth.application.impl;

import com.purwafest.purwafest.auth.application.TokenGeneratorService;
import com.purwafest.purwafest.auth.application.UserService;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.domain.enums.TokenType;
import com.purwafest.purwafest.auth.domain.valueObject.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenGeneratorServiceImpl implements TokenGeneratorService {
    private final JwtEncoder accessTokenEncoder;
    private final JwtEncoder refreshTokenEncoder;
    private final JwtDecoder refreshTokenDecoder;
    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    public TokenGeneratorServiceImpl(
            @Qualifier("jwtEncoder") JwtEncoder accessTokenEncoder,
            @Qualifier("refreshTokenEncoder") JwtEncoder refreshTokenEncoder,
            @Qualifier("refreshTokenDecoder")JwtDecoder refreshTokenDecoder,
            @Qualifier("jwtDecoder") JwtDecoder jwtDecoder,
            UserService userService
            ){
        this.accessTokenEncoder = accessTokenEncoder;
        this.refreshTokenEncoder = refreshTokenEncoder;
        this.refreshTokenDecoder = refreshTokenDecoder;
        this.jwtDecoder = jwtDecoder;
        this.userService = userService;
    }

    @Override
    public Token generateAccessToken(String email, String scopes) {
        User user = userService.getUserByEmail(email);

        Instant now = Instant.now();
        //1hr
        long ACCESS_TOKEN_EXPIRATION_TIME = 3600L;
        Instant expiresAt = now.plusSeconds(ACCESS_TOKEN_EXPIRATION_TIME);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("scope", scopes)
                .claim("kind", TokenType.ACCESS.getType())
                .build();

        JwsHeader header = JwsHeader.with(()->"HS256").build();
        return Token.builder()
                .value(accessTokenEncoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue())
                .tokenType("Bearer")
                .expiresAt(expiresAt.toString())
                .build();
    }

    @Override
    public Token generateAccessToken(String refreshToken) {
        Jwt decodedToken = refreshTokenDecoder.decode(refreshToken);
        if (decodedToken == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        // check if the token is not expired
        if (decodedToken.getExpiresAt() != null && decodedToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        String kind = decodedToken.getClaimAsString("kind");
        Integer userId = Integer.parseInt(decodedToken.getSubject());
        if (!kind.equals(TokenType.REFRESH.getType())) {
            throw new IllegalArgumentException("Invalid token type");
        }
        User user = userService.profile(userId);
        String scopes = user.getUserType().name();
        return generateAccessToken(user.getEmail(), scopes);
    }

    @Override
    public Token generateRefreshToken(String email) {
        User user = userService.getUserByEmail(email);

        Instant now = Instant.now();
        // 7 days
        long REFRESH_TOKEN_EXPIRATION_TIME = 604800L;
        Instant expiresAt = now.plusSeconds(REFRESH_TOKEN_EXPIRATION_TIME);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getId().toString())
                .claim("kind", TokenType.REFRESH.getType())
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS256").build();
        return Token.builder()
                .value(refreshTokenEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue())
                .tokenType("Bearer")
                .expiresAt(expiresAt.toString())
                .build();
    }

    @Override
    public boolean isRefreshToken(String token){
        Jwt decodedToken = refreshTokenDecoder.decode(token);
        String kind = decodedToken.getClaimAsString("kind");

        return kind != null && kind.equals(TokenType.REFRESH.getType());
    }
}
