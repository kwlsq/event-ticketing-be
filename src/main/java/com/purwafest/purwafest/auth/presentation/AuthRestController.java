package com.purwafest.purwafest.auth.presentation;

import com.purwafest.purwafest.auth.application.AuthService;
import com.purwafest.purwafest.auth.presentation.dtos.LoginRequest;
import com.purwafest.purwafest.common.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final AuthService authService;

    public AuthRestController(AuthService authService){this.authService = authService;}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return Response.successfulResponse(
                "Login Success!",
                authService.credentialLogin(request.getEmail(),request.getPassword())
        );
    }
}
