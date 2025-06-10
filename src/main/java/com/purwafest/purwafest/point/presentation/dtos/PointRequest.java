package com.purwafest.purwafest.point.presentation.dtos;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.domain.enums.UserType;
import com.purwafest.purwafest.point.domain.entities.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest {
    private Integer userId;
    private BigInteger amount;
    private Boolean isRedeemed;
    private String pointResource;
    private Instant expiredAt;
    private BigInteger amountUsed;

    public Point toPoint(User user){
        return Point.builder()
                .user(user)
                .amount(amount)
                .isRedeemed(isRedeemed)
                .pointResource(pointResource)
                .expiredAt(expiredAt)
                .amountUsed(amountUsed)
                .build();
    }
}
