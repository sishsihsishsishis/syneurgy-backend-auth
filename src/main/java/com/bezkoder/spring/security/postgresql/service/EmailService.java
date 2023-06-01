package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.dto.EmailDetailsDTO;

public interface EmailService {
    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetailsDTO details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetailsDTO details);

    void sendEmailWithTemplate(EmailDetailsDTO details);

    void sendSimpleEmail(String toEmail, String subject, String content);
    void sendTemplateEmailWithPostmark(String toEmail, Integer templateId, Object templateModel);
}
