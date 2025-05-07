package com.purwafest.purwafest.auth.application.impl;

import com.purwafest.purwafest.auth.application.UserService;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.domain.enums.UserType;
import com.purwafest.purwafest.auth.domain.exceptions.DuplicateUserException;
import com.purwafest.purwafest.auth.domain.exceptions.LoginFailedException;
import com.purwafest.purwafest.auth.domain.exceptions.UserNotFoundException;
import com.purwafest.purwafest.auth.domain.valueObject.AuthUserDetail;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import com.purwafest.purwafest.auth.presentation.dtos.UpdateProfileRequest;
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
            return UserType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user type: " + type);
        }
    }

    @Override
    public User profile(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setPassword(null); // Don't send password to the client
            return foundUser;
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public User updateProfile(UpdateProfileRequest request, Integer userId){
        if(userId == null){
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));

        if(request.getName() != null){
            user.setName(request.getName());
        }
        if(request.getEmail() != null){
            user.setEmail(request.getEmail());
        }
        if(request.getMsisdn() != null){
            user.setMsisdn(request.getMsisdn());
        }

        userRepository.saveAndFlush(user);

        return user;
    }

}
