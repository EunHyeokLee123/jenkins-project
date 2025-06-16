package com.playdata.userservice.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    ADMIN, USER;

    @JsonCreator
    public static Role from(String s) {
        return Role.valueOf(s.toUpperCase());
    }
}
