package com.htec.shelfserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "password_reset_token")
public class PasswordResetTokenEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userDetails;

    public PasswordResetTokenEntity(String token, UserEntity userDetails)
    {
        this.token = token;
        this.userDetails = userDetails;
    }
}
