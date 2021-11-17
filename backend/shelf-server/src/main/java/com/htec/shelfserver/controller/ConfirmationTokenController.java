package com.htec.shelfserver.controller;

import com.htec.shelfserver.service.ConfirmationTokenService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/users/register")
@AllArgsConstructor
public class ConfirmationTokenController {

    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping(path = "/confirmation")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {

        String htmlBody = confirmationTokenService.confirmToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(htmlBody);
    }
}
