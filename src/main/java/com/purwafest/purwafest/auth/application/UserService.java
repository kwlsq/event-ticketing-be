package com.purwafest.purwafest.auth.application;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.presentation.dtos.UpdateProfileRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User register(User request, String registrationType);
    List<User> getAll();
    User getUserByEmail(String email);
    User profile(Integer id);
    User updateProfile(UpdateProfileRequest request, Integer userId);
}
