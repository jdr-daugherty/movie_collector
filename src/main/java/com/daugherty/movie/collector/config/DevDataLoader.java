package com.daugherty.movie.collector.config;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DevDataLoader implements ApplicationRunner {

    private final Movies movies;
    private final Reviews reviews;

    public DevDataLoader(Movies movies, Reviews reviews) {
        this.movies = movies;
        this.reviews = reviews;
    }

    @Override
    public void run(ApplicationArguments args) {
        Movie m1 = movies.save(new Movie("Reservoir Dogs", 500));
        Movie m2 = movies.save(new Movie("Murder She Said", 750));

        Review r1 = reviews.save(new Review("This movie is great!", m1.getId()));
        Review r2 = reviews.save(new Review("Excellent Film!", m2.getId()));
    }
}
