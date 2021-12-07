package com.htec.account.model.response;

import com.htec.account.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseModel {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private RoleEntity role;
}
