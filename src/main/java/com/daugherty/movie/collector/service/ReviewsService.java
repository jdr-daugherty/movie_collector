package com.daugherty.movie.collector.service;

import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.exception.ReviewNotFoundException;

import java.util.List;

/**
 * A service that provides access to reviews of the movies in the collection.
 *
 * @see ReviewDto
 */
public interface ReviewsService {
    /**
     * Returns the full list of reviews for every movie in the collection.
     *
     * @return the List of reviews.
     */
    List<ReviewDto> getAllReviews();

    /**
     * Add the given movie review to the collection.
     *
     * @param reviewDto a ReviewDto describing the movie review
     * @return a MovieDto describing the newly added movie after an ID has been assigned
     */
    ReviewDto addNewReview(ReviewDto reviewDto) throws MovieNotFoundException;

    /**
     * Get the movie review with the given unique ID.
     *
     * @param id the unique ID of the desired review
     * @return a ReviewDto describing the requested movie review
     * @throws ReviewNotFoundException if the requested movie review is not in the collection
     */
    ReviewDto getReviewById(long id) throws ReviewNotFoundException;

    /**
     * Updates the contents (title, body, etc.) of the movie review with the given id.
     * All non-null property values will be copied into the existing movie review.
     *
     * @param source a ReviewDto containing the new review contents to copy into the existing review.
     * @param id     the unique ID of the existing movie review.
     * @return a ReviewDto describing the newly updated movie review
     * @throws ReviewNotFoundException if the requested movie review is not in the collection
     */
    ReviewDto updateReview(ReviewDto source, long id) throws ReviewNotFoundException;

    /**
     * Removes the movie review with the given ID from the collection.
     *
     * @param id the unique ID of the movie review
     */
    void deleteReview(long id);

    /**
     * Returns the list of reviews for the given movie.
     *
     * @param movieId the unique ID of the movie
     * @return the List of reviews.
     */
    List<ReviewDto> findReviewsByMovieId(long movieId);
}
