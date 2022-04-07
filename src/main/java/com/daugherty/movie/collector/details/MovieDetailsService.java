package com.daugherty.movie.collector.details;

import java.util.List;
import java.util.Optional;

/**
 * An external service providing additional details about movies.
 *
 * @see MovieDetails
 */
public interface MovieDetailsService {
    Optional<MovieDetails> getMovieById(long id);

    List<MovieDetails> findMovies(String query);

    Optional<MovieDetails> getMovieByTitle(String title);
}
