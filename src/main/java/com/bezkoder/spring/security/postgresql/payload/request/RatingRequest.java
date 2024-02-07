package com.bezkoder.spring.security.postgresql.payload.request;

public class RatingRequest {
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
