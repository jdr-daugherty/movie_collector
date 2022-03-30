package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;

import java.util.List;

public final class TestObjectMother {

    public static Review reviewWithId() {
        Review expected = reviewWithoutId();
        expected.setId(2345);
        return expected;
    }

    public static Review reviewWithoutId() {
        return new Review("Reservoir Dogs", 1234);
    }

    public static Movie movieWithId() {
        Movie expected = movieWithoutId();
        expected.setId(1234);
        return expected;
    }

    public static Movie movieWithoutId() {
        return new Movie("Reservoir Dogs", 500);
    }

    public static List<Review> listOfReviews() {
        return List.of(
                new Review("Reservoir Dogs", 2345),
                new Review("Murder She Said", 2456));
    }

    public static List<Movie> listOfMovies() {
        return List.of(
                new Movie("Reservoir Dogs", 500),
                new Movie("Murder She Said", 750));
    }
}
