package com.htec.shelfserver.service;

import com.htec.shelfserver.entity.ConfirmationTokenEntity;
import com.htec.shelfserver.repository.ConfirmationTokenRepository;
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
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;
    private final Configuration config;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository,
                                    UserRepository userRepository, Configuration config) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
        this.config = config;
    }

    @Transactional
    public String confirmToken(String token) {
        Optional<ConfirmationTokenEntity> confirmationToken = confirmationTokenRepository.findByToken(token);

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

        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());

        userRepository.enableUser(confirmationToken.get().getUser().getEmail());

        return procesTemplate("Email confirmed");
    }

    private String procesTemplate(String message) {

        String emailContent = "";

        try {

            Template template = config.getTemplate("email-confirmation.html");
            Map<String, Object> model = new HashMap<>();

            model.put("message", message);
            emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        } catch (Exception e) {
        }

        return emailContent;

    }


}
