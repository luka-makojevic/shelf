package com.htec.shelfserver.service;

import com.htec.shelfserver.entity.TokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.repository.TokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final Configuration config;
    private final UserService userService;

    @Autowired
    public TokenService(TokenRepository tokenRepository,
                        UserRepository userRepository,
                        Configuration config,
                        UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.config = config;
        this.userService = userService;
    }

    @Transactional
    public String confirmToken(String token) {
        Optional<TokenEntity> confirmationToken = tokenRepository.findByToken(token);

        if (!confirmationToken.isPresent()) {
            return procesTemplate("Token not found");
        }

        if (confirmationToken.get().getConfirmedAt() != null) {
            return procesTemplate("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.get().getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return procesTemplate("Token expired");
        }

        tokenRepository.updateConfirmedAt(token, LocalDateTime.now());

        userRepository.enableUser(confirmationToken.get().getUser().getEmail());

        return procesTemplate("Email confirmed");
    }

    private String procesTemplate(String message) {

        String emailContent = "";

        try {

            Template template = config.getTemplate("token-confirmation.html");
            Map<String, Object> model = new HashMap<>();

            model.put("message", message);
            emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        } catch (Exception e) {
        }

        return emailContent;

    }


    public String createAndSendToken(String token) {

        Optional<TokenEntity> confirmationToken = tokenRepository.findByToken(token);

        if (!confirmationToken.isPresent()) {
            return procesTemplate("Token not found");
        }

        Optional<UserEntity> userEntity = userRepository.findById(confirmationToken.get().getUser().getId());

        if(userEntity.get().getEmailVerified() == true){
            return procesTemplate("Email already confirmed");
        }

        userService.createAndSendToken(userEntity.get());

        return procesTemplate("Token resent");

    }
}
