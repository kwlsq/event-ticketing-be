package com.purwafest.purwafest.auth.presentation.dtos;

import com.purwafest.purwafest.auth.domain.entities.Referral;
import com.purwafest.purwafest.auth.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralRequest {
    private Integer referrerId; //people who refer
    private Integer refereeId; //people who got referred

    public Referral toReferral(User referrer, User referee) {
        return Referral.builder()
                .referrer(referrer)
                .referee(referee)
                .build();
    }
}
