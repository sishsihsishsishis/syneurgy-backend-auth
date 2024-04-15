package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.exception.ResourceNotFoundException;
import com.bezkoder.spring.security.postgresql.models.Rating;
import com.bezkoder.spring.security.postgresql.payload.request.RatingRequest;
import com.bezkoder.spring.security.postgresql.repository.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Map;


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
    public Rating updateRating(@PathVariable Long id, @RequestBody RatingRequest ratingRequest) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));

        rating.setRatingValue(ratingRequest.getRating());

        return ratingRepository.save(rating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));

        ratingRepository.delete(rating);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/rate")
    public ResponseEntity<List<Rating>> rateMeeting(@RequestBody Map<String, Object> payload) {
        Long userId = Long.parseLong(payload.get("userId").toString());
        Long meetingId = Long.parseLong(payload.get("meetingId").toString());
        List<Map<String, String>> examples = (List<Map<String, String>>) payload.get("examples");

        List<Rating> ratings = ratingRepository.findByUserIdAndMeetingIdOrderByIdAsc(userId, meetingId);
        if (!ratings.isEmpty()) {
            return new ResponseEntity<>(ratings, HttpStatus.OK);
        }

        List<Rating> createdRatings = new ArrayList<>();
        for (Map<String, String> example : examples) {
            Rating rating = new Rating();
            rating.setUserId(userId);
            rating.setMeetingId(meetingId);
            rating.setType(example.get("type"));
            rating.setSubType(example.get("subType"));
            // Set other fields as needed
            ratingRepository.save(rating);
            createdRatings.add(rating);
        }

        return new ResponseEntity<>(createdRatings, HttpStatus.OK);
    }
}