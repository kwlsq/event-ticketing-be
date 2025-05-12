package com.purwafest.purwafest.auth.application.impl;

import com.purwafest.purwafest.auth.application.UserService;
import com.purwafest.purwafest.auth.domain.entities.Referral;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.domain.enums.UserType;
import com.purwafest.purwafest.auth.domain.exceptions.DuplicateUserException;
import com.purwafest.purwafest.auth.domain.exceptions.LoginFailedException;
import com.purwafest.purwafest.auth.domain.exceptions.UserNotFoundException;
import com.purwafest.purwafest.auth.domain.valueObject.AuthUserDetail;
import com.purwafest.purwafest.auth.infrastructure.repository.ReferralRepository;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import com.purwafest.purwafest.auth.presentation.dtos.ReferralRequest;
import com.purwafest.purwafest.auth.presentation.dtos.RegisterRequest;
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
    private final ReferralRepository referralRepository;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ReferralRepository referralRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.referralRepository = referralRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User register(RegisterRequest request, String registrationType) {
        validateUniqueEmail(request.getEmail());

        // Create the user
        User user = createUser(request.toUser(), registrationType);

        // Generate the referral code
        generateReferralCode(user);

        // Handle the referral using the original request
        handleReferral(user, request);

        return user;
    }

    private void validateUniqueEmail(String email) {
        long userWithSameEmailCount = userRepository.countByEmail(email);
        if (userWithSameEmailCount != 0) {
            throw new DuplicateUserException("Account with this email already exists");
        }
    }

    private User createUser(User request, String registrationType) {
        UserType userType = mapToUserType(registrationType);

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setUserType(userType);

        return userRepository.saveAndFlush(request);
    }

    private void generateReferralCode(User user) {
        user.setCode(generateReferralCode(user.getEmail(), user.getId()));
        user = userRepository.saveAndFlush(user);
    }

    private void handleReferral(User user, RegisterRequest request) {
        String referralCode = request.getCode();

        if (referralCode != null && !referralCode.isBlank()) {
            Optional<User> referral = userRepository.findUserByCode(referralCode);
            if (referral.isPresent()) {
                User referrer = referral.get();

                if (!referrer.getId().equals(user.getId())) { // Ensure referrer and referee are not the same
                    Referral referralRequest = new ReferralRequest(referrer.getId(), user.getId()).toReferral(referrer, user);
                    referralRepository.save(referralRequest);
                } else {
                    throw new IllegalArgumentException("A user cannot refer themselves.");
                }
            } else {
                throw new IllegalArgumentException("Referrer not found.");
            }
        }
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
    public User updateProfile(UpdateProfileRequest request, Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getMsisdn() != null) {
            user.setMsisdn(request.getMsisdn());
        }

        userRepository.saveAndFlush(user);

        return user;
    }

    private String generateReferralCode(String email, Integer userId) {
        String prefix = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        prefix = prefix.length() >= 4 ? prefix.substring(0, 4) : String.format("%-4s", prefix).replace(' ', 'X');
        return prefix + String.format("%04d", userId);
    }

}