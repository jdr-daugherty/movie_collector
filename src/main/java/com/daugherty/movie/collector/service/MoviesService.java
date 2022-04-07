package com.daugherty.movie.collector.service;

import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;

import java.util.List;

/**
 * A service that provides access to a collection of movies and details about each.
 *
 * @see MovieDto
 * @see MovieDetailsDto
 */
public interface MoviesService {
    /**
     * Returns the full list of movies in the collection.
     *
     * @return the List of movies.
     */
    List<MovieDto> getAllMovies();

    /**
     * Get the basic information about the requested movie.
     *
     * @param id the unique ID of the desired movie
     * @return a MovieDto describing the requested movie
     * @throws MovieNotFoundException if the requested movie is not in the collection
     */
    MovieDto getMovieById(long id) throws MovieNotFoundException;

    /**
     * Add the given movie to the collection.
     *
     * @param movieDto a MovieDto describing the movie
     * @return a MovieDto describing the newly added movie after an ID has been assigned
     */
    MovieDto addNewMovie(MovieDto movieDto);

    /**
     * Removes the movie with the given ID from the collection.
     *
     * @param id the unique ID of the movie
     */
    void deleteMovie(long id);

    /**
     * Get the mmost detailed information about the requested movie.
     *
     * @param movieId the unique ID of the desired movie
     * @return a {@link MovieDetailsDto} containing the movie details
     * @throws MovieNotFoundException if the requested movie is not in the collection
     */
    MovieDetailsDto getMovieDetails(long movieId) throws MovieNotFoundException;
}
