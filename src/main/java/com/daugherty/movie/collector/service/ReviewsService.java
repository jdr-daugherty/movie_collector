package com.daugherty.movie.collector.service;

import com.daugherty.movie.collector.dto.ReviewDto;

import java.util.List;

public interface ReviewsService {
    List<ReviewDto> getAllReviews();

    ReviewDto addNewReview(ReviewDto reviewDto);

    ReviewDto getReviewById(long id);

    ReviewDto updateReview(ReviewDto source, long id);

    void deleteReview(long id);

    List<ReviewDto> findReviewsByMovieId(long movieId);
}
