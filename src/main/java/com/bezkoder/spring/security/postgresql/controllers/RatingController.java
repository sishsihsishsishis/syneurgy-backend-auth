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
        List<Map<String, Object>> examples = (List<Map<String, Object>>) payload.get("examples");
        boolean isDemo = (boolean) payload.get("isDemo");
        List<Rating> createdRatings = new ArrayList<>();
        if (isDemo) {
            List<Rating> ratings = ratingRepository.findByUserIdAndMeetingIdOrderByIdAsc(userId, meetingId);
            if (!ratings.isEmpty()) {
                return new ResponseEntity<>(ratings, HttpStatus.OK);
            }

            for (Map<String, Object> example : examples) {
                Rating rating = new Rating();
                rating.setUserId(userId);
                rating.setMeetingId(meetingId);
                rating.setKeyword(example.get("keyword").toString());
                rating.setDemo(true);
                // Set other fields as needed
                ratingRepository.save(rating);
                createdRatings.add(rating);
            }

            return new ResponseEntity<>(createdRatings, HttpStatus.OK);
        }
        else { // just 10 examples
            for(Map<String, Object> example: examples) {
                Double starts;
                Double ends;
                Object startsObj = example.get("starts");
                if (startsObj instanceof Integer) {
                    starts = ((Integer) startsObj).doubleValue();
                } else if (startsObj instanceof Long) {
                    starts = ((Long) startsObj).doubleValue();
                } else if (startsObj instanceof Double) {
                    starts = (Double) startsObj;
                } else {
                    throw new IllegalArgumentException("Unexpected type: starts" );
                }


                Object endsObj = example.get("ends");
                if (endsObj instanceof Integer) {
                    ends = ((Integer) endsObj).doubleValue();
                } else if (endsObj instanceof Long) {
                    ends = ((Long) endsObj).doubleValue();
                } else if (endsObj instanceof Double) {
                    ends = (Double) endsObj;
                } else {
                    throw new IllegalArgumentException("Unexpected type: ends" );
                }

                Integer integerValue = (Integer) example.get("meetingId");
                Long meetingId1 = integerValue.longValue();

                List<Rating> ratings = ratingRepository.findByUserIdAndMeetingIdAndStartsAndEndsOrderByIdAsc(userId, meetingId1, starts, ends);
                if (!ratings.isEmpty() ){
                    Rating rating1 = ratings.get(0);
                    createdRatings.add(rating1);
                } else {
                    Rating rating = new Rating();
                    rating.setUserId(userId);
                    rating.setMeetingId(meetingId1);
                    rating.setKeyword(example.get("keyword").toString());
                    rating.setStarts(starts);
                    rating.setEnds(ends);
                    rating.setDemo(false);
                    ratingRepository.save(rating);
                    createdRatings.add(rating);
                }
            }
        }
        return new ResponseEntity<>(createdRatings, HttpStatus.OK);
    }
}