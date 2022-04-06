package com.daugherty.movie.collector.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String message) {
        super(message);
    }

    public MovieNotFoundException() {
        this("Movie not found.");
    }
}
