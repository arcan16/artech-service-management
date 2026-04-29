package com.Ar_Tech.infra.mail.services;

import jakarta.mail.MessagingException;

import java.io.File;

public interface IMailService {

    void sendSingleMail(String [] toUser, String subject, String message);

    void sendMailWithFiles(String [] toUser, String subject, String messaje, File[] files) throws MessagingException;
}
