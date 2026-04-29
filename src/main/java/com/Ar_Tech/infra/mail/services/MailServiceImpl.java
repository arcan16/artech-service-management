package com.Ar_Tech.infra.mail.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class MailServiceImpl implements IMailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${java.mail.sender}")
    private String mailSender;

    @Override
    public void sendSingleMail(String[] toUser, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailSender);
        mailMessage.setTo(toUser);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }

    @Override
    public void sendMailWithFiles(String[] toUser, String subject, String message, File[] files) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

        messageHelper.setFrom(mailSender);
        messageHelper.setTo(toUser);
        messageHelper.setSubject(subject);
        messageHelper.setText(message);
        for(File f: files)
            messageHelper.addAttachment(f.getName(), f);

        javaMailSender.send(mimeMessage);
    }
}
