package com.htec.shelfserver.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestModel {

    private String email;
    private String password;
    private String salt;
}
