package com.purwafest.purwafest.auth.presentation;

import com.purwafest.purwafest.auth.application.UserService;
import com.purwafest.purwafest.auth.presentation.dtos.RegisterRequest;
import com.purwafest.purwafest.auth.presentation.dtos.UpdateProfileRequest;
import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.common.security.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getProfile() {
        Integer id = Claims.getUserId();
        return Response.successfulResponse(
                "Get profile Success",
                userService.profile(id)
        );
    }

    @PostMapping("/register/{registrationType}")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, @PathVariable String registrationType) {
        return Response.successfulResponse(
                "Register Success",
                userService.register(request, registrationType)
        );
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request){
        Integer id = Claims.getUserId();
        return Response.successfulResponse(
                "User updated successfully",
                userService.updateProfile(request,id)
        );
    }
}
