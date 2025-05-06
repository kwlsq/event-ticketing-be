package com.purwafest.purwafest.auth.application.impl;

import com.purwafest.purwafest.auth.application.UserService;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.domain.enums.UserType;
import com.purwafest.purwafest.auth.domain.exceptions.DuplicateUserException;
import com.purwafest.purwafest.auth.domain.exceptions.LoginFailedException;
import com.purwafest.purwafest.auth.domain.exceptions.UserNotFoundException;
import com.purwafest.purwafest.auth.domain.valueObject.AuthUserDetail;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    //Dependency injection from repositories
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User register(User request, String registrationType){
        UserType userType = mapToUserType(registrationType);

        long userWithSameEmailCount = userRepository.countByEmail(request.getEmail());
        if (userWithSameEmailCount != 0) {
            throw new DuplicateUserException("Account with this email already exists");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        request.setUserType(userType);
        return userRepository.save(request);
    }

    @Override
    public User login(User request){

        Optional<User> foundUser = userRepository.findUserByEmail(request.getEmail());
        if (foundUser.isEmpty() || !request.getPassword().equals(foundUser.get().getPassword())) {
            throw new LoginFailedException("Invalid email or password");
        }

        return foundUser.get();
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user = getUserByEmail(email);
       AuthUserDetail userDetails = new AuthUserDetail();
       userDetails.setEmail(email);
       userDetails.setPassword(user.getPassword());
       userDetails.setType(user.getUserType());
       return userDetails;
    }

    private UserType mapToUserType(String type) {
        try {
            return UserType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user type: " + type);
        }
    }

}
