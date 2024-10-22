package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.UserAnswer;
import com.bezkoder.spring.security.postgresql.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public UserAnswer saveUserAnswer(UserAnswer userAnswer) {
        return userAnswerRepository.save(userAnswer);
    }

    public UserAnswer getUserAnswerById(Long id) {
        return userAnswerRepository.findById(id).orElse(null);
    }

    public List<UserAnswer> saveAllUserAnswers(List<UserAnswer> userAnswers) {
        return userAnswerRepository.saveAll(userAnswers);
    }

    public List<UserAnswer> getAnswersByUserId(Long userId) {
        return userAnswerRepository.findByUserIdOrderByQuestionIdAsc(userId);
    }
}