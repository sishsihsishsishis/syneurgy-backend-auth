package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Challenge;
import com.bezkoder.spring.security.postgresql.models.Habit;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.HabitRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/habits")
@Api(tags = "Habits")
public class HabitController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    HabitRepository habitRepository;

    @GetMapping
    @ApiOperation("Get All Habits, required user token")
    public ResponseEntity<?> getAllHabits() {

        List<Habit> habits = new ArrayList<>();
        habitRepository.findAllByOrderByIdDesc().forEach(habits::add);

        if (habits.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(habits, HttpStatus.OK);
    }

    @GetMapping("/byCategory/{category}")
    public ResponseEntity<?> getHabitsByCategory(@PathVariable Long category) {


        List<Habit> habits = habitRepository.findByCategory(category);
        return ResponseEntity.ok(habits);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get habit by id, required user token")
    public ResponseEntity<?> getHabitById(@PathVariable Long id) {
        Optional<Habit> habit = habitRepository.findById(id);
        return habit.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation("Create habit")
    public ResponseEntity<?> createHabit(@RequestBody Habit habit) {
        return ResponseEntity.ok(habitRepository.save(habit));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update challenge by id")
    public ResponseEntity<?> updateHabit(@PathVariable Long id, @RequestBody Habit updatedHabit) {
        Optional<Habit> existingHabit = habitRepository.findById(id);
        if (existingHabit.isPresent()) {
            updatedHabit.setId(id);
            return ResponseEntity.ok(habitRepository.save(updatedHabit));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete Challenge by id, required user token")
    public ResponseEntity<?> deleteHabit(@PathVariable Long id) {
        Optional<Habit> habit = habitRepository.findById(id);
        if (habit.isPresent()) {
            habitRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
