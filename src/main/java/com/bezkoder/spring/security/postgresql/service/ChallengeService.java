package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.dto.ChallengeWithUsersDto;
import com.bezkoder.spring.security.postgresql.repository.ChallengeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigInteger;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeService {
    @Autowired
    private ChallengeRepository challengeRepository;

    public List<ChallengeWithUsersDto> getChallengesWithUsers() {
        List<Object[]> results = challengeRepository.getChallengesWithUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        return results.stream()
                .map(result -> new ChallengeWithUsersDto(
                        (BigInteger) result[0],    // challenge_id
                        (String) result[1],  // challenge_name
                        (String) result[2],  // challenge_description
                        objectMapper.convertValue(result[3], String.class)
                ))
                .collect(Collectors.toList());
    }
}
