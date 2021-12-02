package com.htec.shelfserver.annotation;

import lombok.Getter;

@Getter
public enum Roles {
    SUPER_ADMIN(1),
    MODERATOR(2),
    USER(3);

    private final Integer value;

    Roles(Integer value) {
        this.value = value;
    }
}
