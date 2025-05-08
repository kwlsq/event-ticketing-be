package com.purwafest.purwafest.auth.application;

import com.purwafest.purwafest.auth.presentation.dtos.LoginResponse;

public interface AuthService {
    LoginResponse credentialLogin(String email, String password);
}
