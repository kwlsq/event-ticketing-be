package com.purwafest.purwafest.auth.presentation;

import com.purwafest.purwafest.auth.application.UserService;
import com.purwafest.purwafest.auth.presentation.dtos.RegisterRequest;
import com.purwafest.purwafest.common.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        return Response.successfulResponse(
                "Get all Success",
                userService.getAll()
        );
    }

    @PostMapping("/register/{registrationType}")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, @PathVariable String registrationType) {
        return Response.successfulResponse(
                "Register Success",
                userService.register(request.toUser(), registrationType)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
        return Response.successfulResponse(
                "Login Success",
                userService.login(request.toUser())
        );
    }
}
