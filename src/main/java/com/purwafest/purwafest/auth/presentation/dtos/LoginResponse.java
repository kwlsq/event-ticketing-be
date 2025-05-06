package com.purwafest.purwafest.auth.presentation.dtos;

import com.purwafest.purwafest.auth.domain.valueObject.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
  private Token accessToken;
  private Token refreshToken;
}
