package com.daugherty.movie.collector.details;

import java.util.List;
import java.util.Optional;

public interface MovieDetailsService {
    Optional<MovieDetails> getMovieById(long id);

    List<MovieDetails> findMovies(String query);

    Optional<MovieDetails> getMovieByTitle(String title);
}
