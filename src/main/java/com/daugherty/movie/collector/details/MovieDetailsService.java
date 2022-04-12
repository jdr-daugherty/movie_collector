package com.daugherty.movie.collector.details;

import java.util.List;
import java.util.Optional;

/**
 * An external service providing additional details about movies.
 * <p>
 * Note that methods in this service may synchronously contact an external
 * movie details service or retrieve the details from cache depending on the
 * details service implementation.
 *
 * @see MovieDetails
 */
public interface MovieDetailsService {
    /**
     * Retrieves the details of the movie with the given unique identifier
     * assigned by the details service.
     *
     * @param id the unique ID assigned by the details service.
     * @return an Optional containing the {@link MovieDetails details} of
     * the requested movie
     */
    Optional<MovieDetails> getMovieById(long id);

    /**
     * Retrieves details for each movie that matches the given query string.
     * The query will be compared to the movie's title, at a minimum, but
     * may also be applied to other details.
     *
     * @param query a String containing the query
     * @return the List of {@link MovieDetails movies} that were matched by
     * the given query.
     */
    List<MovieDetails> findMovies(String query);

    /**
     * Retrieves the details of the movie with the given title.
     *
     * @param title a String containing the exact title of the movie
     * @return an Optional containing the {@link MovieDetails details} of
     * the requested movie
     */
    Optional<MovieDetails> getMovieByTitle(String title);
}
