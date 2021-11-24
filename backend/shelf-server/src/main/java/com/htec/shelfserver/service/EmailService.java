package com.htec.shelfserver.service;

import com.htec.shelfserver.exceptionSupplier.ExceptionSupplier;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final Configuration config;

    @Autowired
    public EmailService(JavaMailSender mailSender, Configuration config) {
        this.mailSender = mailSender;
        this.config = config;
    }

    @Async
    public void sendEmail(String to, Map<String, Object> model , String htmlTemplate , String subject) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());


            Template template = config.getTemplate(htmlTemplate);
            String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setText(emailContent, true);
            helper.setTo(to);
            helper.setSubject(subject);

            mailSender.send(mimeMessage);

        } catch (MessagingException | IOException | TemplateException e) {

            throw ExceptionSupplier.emailFailedToSend.get();

        }


    }
}
