package com.purwafest.purwafest.referral.domain.exceptions;

public class ReferralNotFoundException extends RuntimeException {
    public ReferralNotFoundException(String message) {
        super(message);
    }
}
