package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.exception.ResourceNotFoundException;
import com.bezkoder.spring.security.postgresql.models.Rating;
import com.bezkoder.spring.security.postgresql.repository.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @PostMapping
    public Rating createRating(@RequestBody Rating rating) {
        return ratingRepository.save(rating);
    }

    @GetMapping("/{id}")
    public Rating getRatingById(@PathVariable Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));
    }

    @PutMapping("/{id}")
    public Rating updateRating(@PathVariable Long id, @RequestBody Rating ratingDetails) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));

        rating.setMeetingId(ratingDetails.getMeetingId());
        rating.setUserId(ratingDetails.getUserId());
        rating.setRatingValue(ratingDetails.getRatingValue());

        return ratingRepository.save(rating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));

        ratingRepository.delete(rating);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkOrCreate")
    public ResponseEntity<?> checkOrCreateRating(@RequestParam Long userId, @RequestParam Long meetingId, @RequestParam int count) {
        List<Rating> existingRatings = ratingRepository.findByUserIdAndMeetingId(userId, meetingId);
        if (existingRatings.isEmpty()) {
            // If no records found, create 'count' records with given userId and meetingId
            for (int i = 0; i < count; i++) {
                Rating rating = new Rating();
                rating.setUserId(userId);
                rating.setMeetingId(meetingId);
                ratingRepository.save(rating);
            }
        } else {
            return new ResponseEntity<>(existingRatings, HttpStatus.OK);
        }
        existingRatings = ratingRepository.findByUserIdAndMeetingId(userId, meetingId);
        return new ResponseEntity<>(existingRatings, HttpStatus.OK);
    }
}