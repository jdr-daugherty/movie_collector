package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import org.themoviedb.api.dto.TmdbMovie;

import java.util.Date;

public final class TestObjectMother {

    public static Review reviewWithId() {
        Review expected = reviewWithoutId();
        expected.setId(2345);
        return expected;
    }

    public static Review reviewWithoutId() {
        return new Review("This movie is great!", 1234);
    }

    public static Movie movieWithId() {
        Movie expected = movieWithoutId();
        expected.setId(1234);
        return expected;
    }

    public static TmdbMovie tmdbMovie() {
        TmdbMovie m = new TmdbMovie();
        m.setReleaseDate(new Date());
        m.setTitle("Reservoir Dogs");
        m.setId(500);
        m.setTagline("Every dog has his day.");
        m.setAdult(true);
        m.setRuntime(99);
        return m;
    }

    public static Movie movieWithoutId() {
        return new Movie("Reservoir Dogs", 500);
    }
}
