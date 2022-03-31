package com.daugherty.movie.collector.controller;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String message) {
        super(message);
    }

    public MovieNotFoundException() {
        this("Movie not found.");
    }
}
