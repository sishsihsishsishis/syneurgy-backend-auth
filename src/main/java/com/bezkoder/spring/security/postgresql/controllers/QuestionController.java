package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Question;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.AnswerRequest;
import com.bezkoder.spring.security.postgresql.payload.request.InviteRequest;
import com.bezkoder.spring.security.postgresql.payload.request.TeamRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserInfoRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserInfoResponse;
import com.bezkoder.spring.security.postgresql.repository.QuestionRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class QuestionController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;
    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        List<Question> questions = questionRepository.findAll();
        List <Question> questions1 =new ArrayList<>();
        for (int i=0; i<3; i++) {
            questions1.add(questions.get(i));
        }
        return new ResponseEntity<>(questions1, HttpStatus.OK);
    }

    @PostMapping("/questions")
    public ResponseEntity<?> answerQuestions(@Valid @RequestBody AnswerRequest answerRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        String answers = answerRequest.getAnswers();
        String user_answer = currentUser.getAnswers();
        if (user_answer == null || user_answer.length() == 0) {
            currentUser.setAnswers(answers);
            currentUser.setStep(currentUser.getStep() + 1);
            User newUser = userRepository.save(currentUser);
            return ResponseEntity.ok(new UserInfoResponse(newUser.getFirstName(), newUser.getLastName(), newUser.getCountry(), newUser.getCountryCode(), newUser.getCompany(), newUser.getPosition(), newUser.getStep(), newUser.getPhoto()));
        }
         else {
             user_answer = user_answer + ":" + answers;
             currentUser.setAnswers(user_answer);
            User newUser = userRepository.save(currentUser);
            return ResponseEntity.ok(new UserInfoResponse(newUser.getFirstName(), newUser.getLastName(), newUser.getCountry(), newUser.getCountryCode(), newUser.getCompany(), newUser.getPosition(), newUser.getStep(), newUser.getPhoto()));
        }
    }

}
