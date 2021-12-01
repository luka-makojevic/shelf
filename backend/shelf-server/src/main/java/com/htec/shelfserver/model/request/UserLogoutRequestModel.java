package com.htec.shelfserver.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLogoutRequestModel {
    private String jwtToken;
    private String jwtRefreshToken;
}
