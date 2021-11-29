package com.htec.shelfserver.model.response;

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
    private Long role;
}
