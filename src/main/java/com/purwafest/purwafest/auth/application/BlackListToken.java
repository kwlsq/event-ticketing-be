package com.purwafest.purwafest.auth.application;

public interface BlackListToken {
    void addToBlackList(String token);
    boolean isBlackListed(String token);
}
