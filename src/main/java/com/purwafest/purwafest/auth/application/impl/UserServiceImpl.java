package com.purwafest.purwafest.auth.application.impl;

import com.purwafest.purwafest.auth.application.UserService;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.domain.enums.UserType;
import com.purwafest.purwafest.auth.domain.exceptions.DuplicateUserException;
import com.purwafest.purwafest.auth.domain.exceptions.LoginFailedException;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    //Dependency injection from repositories
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User register(User request, String registrationType){
        // Convert registrationType to uppercase and map it to the UserType enum
        UserType userType = UserType.valueOf(registrationType.toUpperCase());

        //Check if user with submitted email exists
        long userWithSameEmailCount = userRepository.countByEmailAndUserType(request.getEmail(),userType);
        if (userWithSameEmailCount != 0) {
            throw new DuplicateUserException(registrationType + " with this email already exists");
        }
        //Set type base on registrationType, save to DB, then return the result of newly registered user
        request.setUserType(registrationType.equalsIgnoreCase("user")? UserType.USER:UserType.ORGANIZER);
        return userRepository.save(request);
    }

    @Override
    public User login(User request, String loginType){
        // Convert registrationType to uppercase and map it to the UserType enum
        UserType userType = UserType.valueOf(loginType.toUpperCase());

        Optional<User> foundUser = userRepository.findUserByEmailAndUserType(request.getEmail(),userType);
//        authencticate

        if(request.getPassword().equals(foundUser.get().getPassword())){
            return foundUser.get();
        } else {
            throw new LoginFailedException("Invalid email or password");
        }
    }

}
