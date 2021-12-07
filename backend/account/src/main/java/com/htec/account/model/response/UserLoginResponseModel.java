package com.htec.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseModel {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String jwtToken;
    private String jwtRefreshToken;
    private Long role;
}
