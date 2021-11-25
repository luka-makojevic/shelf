package com.htec.shelfserver.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String bearerToken;
}
