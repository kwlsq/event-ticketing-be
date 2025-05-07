package com.purwafest.purwafest.auth.presentation;

import com.purwafest.purwafest.auth.application.AuthService;
import com.purwafest.purwafest.auth.application.TokenGeneratorService;
import com.purwafest.purwafest.auth.presentation.dtos.LoginRequest;
import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.common.security.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final AuthService authService;
    private final TokenGeneratorService tokenGeneratorService;

    public AuthRestController(AuthService authService, TokenGeneratorService tokenGeneratorService){
        this.authService = authService;
        this.tokenGeneratorService = tokenGeneratorService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return Response.successfulResponse(
                "Login Success!",
                authService.credentialLogin(request.getEmail(),request.getPassword())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String refreshToken) {
        String accessToken = Claims.getTokenValue();
        authService.logout(accessToken, refreshToken);
        return Response.successfulResponse("User logged out successfully", null);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        return Response.successfulResponse("Token refreshed successfully", tokenGeneratorService.generateAccessToken(refreshToken));
    }
}
