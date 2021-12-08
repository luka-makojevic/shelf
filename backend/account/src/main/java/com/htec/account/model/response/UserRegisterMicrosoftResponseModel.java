package com.htec.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterMicrosoftResponseModel {
    private String givenName;
    private String surname;
    private String mail;
}
