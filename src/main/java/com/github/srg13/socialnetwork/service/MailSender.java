package com.github.srg13.socialnetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

    @Value("${activate.link}")
    private String activationLink;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Autowired
    private JavaMailSender mailSender;

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailUsername);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public void sendActivationMail(String username, String emailTo, String activationCode) {
        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to SCN. Please, visit next link: %s/%s",
                username, activationLink, activationCode
        );

        send(emailTo, "Account activation", message);
    }
}
