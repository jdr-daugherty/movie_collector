package com.daugherty.movie.collector.controller;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException() {
        this("Review not found.");
    }
}
