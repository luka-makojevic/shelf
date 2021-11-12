package com.htec.shelfserver.dto;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Date;

public class UserDTO implements Serializable {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
//    private String salt;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Boolean emailVerified;
    private Long roleId;
}
