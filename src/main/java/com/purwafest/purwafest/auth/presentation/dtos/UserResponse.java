package com.purwafest.purwafest.auth.presentation.dtos;

import com.purwafest.purwafest.auth.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String email;
    private String msisdn;

    public static UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setMsisdn(user.getMsisdn());
        return userResponse;
    }
}
