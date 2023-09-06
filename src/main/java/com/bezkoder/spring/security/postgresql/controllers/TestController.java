package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Team;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.TeamRequest;
import com.bezkoder.spring.security.postgresql.repository.TeamRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import com.postmarkapp.postmark.client.data.model.templates.TemplatedMessage;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {

//        ApiClient client = Postmark.getApiClient("2274a4ca-df74-4850-8b4c-06d1da6c14a2");
//        Message message = new Message("notifications@syneurgy.com", "anbraun117@gmail.com", "Join your team!", " invited you to collaborate in " + " \n http://127.0.0.1:5173/confirm-invitation?token=");
//        try {
//            com.postmarkapp.postmark.client.data.model.message.MessageResponse response = client.deliverMessage(message);
//
//            int sss = 1;
//        } catch (PostmarkException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return "Public Content.";
    }

    @GetMapping("/test")
    public String test() {
        ApiClient client = Postmark.getApiClient("2274a4ca-df74-4850-8b4c-06d1da6c14a2");
        TemplatedMessage message = new TemplatedMessage("notifications@syneurgy.com", "anbraun117@gmail.com");
        message.setTemplateId(31950979);

        HashMap model = new HashMap<String, Object>();
        model.put("invite_receiver_email", "anbraun117@gmail.com");
        model.put("action_url", "http://localhost:5173");
        message.setTemplateModel(model);

        try {
            MessageResponse response = client.deliverMessageWithTemplate(message);
            int sss = 1;
        } catch (PostmarkException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "Private Content";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
