package com.htec.shelfserver.dto;

import com.htec.shelfserver.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String salt;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Boolean emailVerified;
    private RoleEntity role;
}
