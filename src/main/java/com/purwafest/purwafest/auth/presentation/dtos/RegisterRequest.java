package com.purwafest.purwafest.auth.presentation.dtos;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.domain.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String msisdn;
    private UserType userType;
    private String code;

    public User toUser() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .msisdn(msisdn)
                .userType(userType)
                .code(code)
                .build();
    }
}
