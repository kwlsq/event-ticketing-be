package com.purwafest.purwafest.auth.application;

import com.purwafest.purwafest.auth.domain.entities.User;

import java.util.List;

public interface UserService {
    User register(User request, String registrationType);
    List<User> getAll();

}
