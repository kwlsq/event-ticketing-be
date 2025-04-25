package auth.presentation.dtos;

import auth.domain.entities.User;
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

    public User toUser() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .msisdn(msisdn)
                .build();
    }
}
