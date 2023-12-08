package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.dto.ComponentWithUsersDto;
import com.bezkoder.spring.security.postgresql.repository.ComponentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.ObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComponentService {
    @Autowired
    private ComponentRepository componentRepository;

    public List<ComponentWithUsersDto> getComponentsWithUsers() {
        List<Object[]> results = componentRepository.getComponentsWithUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        return results.stream()
                .map(result -> new ComponentWithUsersDto(
                        (BigInteger) result[0], // component_id
                        (String) result[1], // component_name
                        objectMapper.convertValue(result[2], String.class)
                ))
                .collect(Collectors.toList());
    }
}
