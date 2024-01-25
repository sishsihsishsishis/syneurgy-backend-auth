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
import io.swagger.annotations.Api;
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
@Api(tags = "Question")
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
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();
        String answers = currentUser.getAnswers();
        Integer pageIndex = 0;
        if (answers != null) {
            String[] arrAnswers = answers.split(":");
            String answer = arrAnswers[arrAnswers.length - 1];
            String[] questions = answer.split("-");
            String strQuestionId = questions[0];
            Integer questionId = Integer.valueOf(strQuestionId);
            Integer ids = questionId / 4;
            pageIndex = ids * 4;
        }
        List<Question> questions = questionRepository.findAll();
        List<Question> questions1 = new ArrayList<>();
        for (int i = pageIndex; i < pageIndex + 4; i++) {
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
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();

        String answers = answerRequest.getAnswers();
        String user_answer = currentUser.getAnswers();
        if (user_answer == null || user_answer.length() == 0) {
            currentUser.setAnswers(answers);
            User newUser = userRepository.save(currentUser);
            return ResponseEntity.ok(new UserInfoResponse(newUser.getFirstName(), newUser.getLastName(), newUser.getCountry(), newUser.getCountryCode(), newUser.getCompany(), newUser.getPosition(), newUser.getStep(), newUser.getPhoto(), newUser.getAnswers()));
        } else {

            String[] arrUserAnswers = user_answer.split(":");
            String[] arrAnswered = answers.split("-");
            String newQuestionId = arrAnswered[0];
            boolean isChanged = false;
            user_answer = "";
            for (int i = 0; i < arrUserAnswers.length; i++) {
                String answer = arrUserAnswers[i];
                String[] questions = answer.split("-");
                String questionId = questions[0];
                if (questionId.equals(newQuestionId)) {

                    isChanged = true;
                    if (user_answer.length() == 0) {
                        user_answer = answers;
                    } else {
                        user_answer = user_answer + ":" + answers;
                    }
                } else {
                    if (user_answer.length() == 0) {
                        user_answer = answer;
                    } else {
                        user_answer = user_answer + ":" + answer;
                    }
                }
            }

            if (isChanged) {


            } else {
                user_answer = user_answer + ":" + answers;
            }


            currentUser.setAnswers(user_answer);
            User newUser = userRepository.save(currentUser);
            return ResponseEntity.ok(new UserInfoResponse(newUser.getFirstName(), newUser.getLastName(), newUser.getCountry(), newUser.getCountryCode(), newUser.getCompany(), newUser.getPosition(), newUser.getStep(), newUser.getPhoto(), newUser.getAnswers()));
        }
    }

}
