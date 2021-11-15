package com.htec.shelfserver.service;

import com.htec.shelfserver.entity.ConfirmationTokenEntity;
import com.htec.shelfserver.repository.ConfirmationTokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository,
                                    UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<String> confirmToken(String token) {
        ConfirmationTokenEntity confirmationToken = this.getToken(token);

        if (confirmationToken == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.NOT_FOUND);
        }

        if (confirmationToken.getConfirmedAt() != null) {
            return new ResponseEntity<>("Email already confirmed", HttpStatus.METHOD_NOT_ALLOWED);
        }

        Date expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.before(new Date())) {
            return new ResponseEntity<>("Token expired", HttpStatus.UNAUTHORIZED);
        }

        this.setConfirmedAt(token);

        this.enableUser(confirmationToken.getUser().getEmail());

        return new ResponseEntity<>("Email confirmed", HttpStatus.ACCEPTED);
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public ConfirmationTokenEntity getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, new Date());
    }
}
