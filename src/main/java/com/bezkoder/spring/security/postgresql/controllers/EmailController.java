package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.EmailDetailsDTO;
import com.bezkoder.spring.security.postgresql.service.EmailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Sending a simple Email
    @PostMapping("api/sendMail")
    public String
    sendMail(@RequestBody EmailDetailsDTO details)
    {
        String status
                = emailService.sendSimpleMail(details);

        return status;
    }

    // Sending email with attachment
    @PostMapping("api/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetailsDTO details)
    {
        String status
                = emailService.sendMailWithAttachment(details);

        return status;
    }
}
