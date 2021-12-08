package com.htec.account.annotation;

import lombok.Getter;

@Getter
public enum Roles {
    SUPER_ADMIN(1L),
    MODERATOR(2L),
    USER(3L);

    private final Long value;

    Roles(Long value) {
        this.value = value;
    }
}
