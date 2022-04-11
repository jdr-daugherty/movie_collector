package com.daugherty.movie.collector.test.service;

import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.exception.ReviewNotFoundException;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import com.daugherty.movie.collector.service.ReviewsService;
import com.daugherty.movie.collector.service.ReviewsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.daugherty.movie.collector.test.mother.MovieMother.movieWithId;
import static com.daugherty.movie.collector.test.mother.ReviewMother.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ReviewsServiceTests {

    @MockBean
    private Movies movies;

    @MockBean
    private Reviews reviews;

    private ReviewsService service;

    @BeforeEach
    private void setup() {
        service = new ReviewsServiceImpl(movies, reviews);
    }

    @Test
    void getAllReviewsWithNoReviews() {
        assertTrue(service.getAllReviews().isEmpty());
    }

    @Test
    void getAllReviews() {
        List<Review> expected = List.of(reviewWithId());
        when(reviews.findAll()).thenReturn(expected);

        List<ReviewDto> allReviews = service.getAllReviews();
        verify(reviews, times(1)).findAll();

        ReviewDto result = allReviews.get(0);
        assertEquals(expected.get(0).getTitle(), result.getTitle());
    }

    @Test
    void findReviewsByMovieId() {
        List<Review> expected = List.of(reviewWithId());
        when(reviews.findByMovieId(2)).thenReturn(expected);

        List<ReviewDto> allReviews = service.findReviewsByMovieId(2);
        verify(reviews, times(1)).findByMovieId(2);

        ReviewDto result = allReviews.get(0);
        assertEquals(expected.get(0).getTitle(), result.getTitle());
    }

    @Test
    void getReviewByIdNotFound() {
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> service.getReviewById(Long.MIN_VALUE));
    }

    @Test
    void getReviewById() {
        Review expected = reviewWithId();
        when(reviews.findById(expected.getId())).thenReturn(Optional.of(expected));

        ReviewDto result = service.getReviewById(expected.getId());
        verify(reviews, times(1)).findById(expected.getId());

        // Verify model (expected) to DTO (result) conversion
        assertEqual(expected, result);
    }

    @Test
    void addNewReview() {
        Movie movie = movieWithId();
        when(movies.existsById(movie.getId())).thenReturn(true);

        ReviewDto expected = reviewDtoWithoutId();
        expected.setMovieId(movie.getId());
        expected.setReviewed(new Date());

        when(reviews.save(Mockito.any(Review.class))).then(returnsFirstArg());

        ReviewDto result = service.addNewReview(expected);

        verify(movies, times(1)).existsById(movie.getId());
        verify(reviews, times(1)).save(Mockito.any(Review.class));

        // Verify both model (expected) to DTO (result) conversion and DTO to model conversion.
        assertEquals(expected, result);
    }

    @Test
    void addNewReviewMovieNotFound() {
        when(movies.existsById(Mockito.anyLong())).thenReturn(false);

        final ReviewDto reviewDto = reviewDtoWithoutId();
        assertThrows(MovieNotFoundException.class,
                () -> service.addNewReview(reviewDto));

        verify(movies, times(1)).existsById(Mockito.anyLong());
    }

    @Test
    void updateReviewNotFound() {
        when(reviews.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final ReviewDto reviewDto = reviewDtoWithId();
        assertThrows(ReviewNotFoundException.class,
                () -> service.updateReview(reviewDto, reviewDto.getId()));

        verify(reviews, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void updateReview() {
        Review existing = reviewWithId();
        existing.setReviewed(new Date(1641056400));// 2022-01-01-1200
        ReviewDto parameter = reviewDtoWithId();
        parameter.setReviewed(new Date(1643734800));// 2022-02-01-1200
        parameter.setTitle("New Review Title!");
        parameter.setBody("New Review Body");
        parameter.setRating(10);

        when(reviews.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(reviews.save(Mockito.any(Review.class))).then(returnsFirstArg());

        ReviewDto result = service.updateReview(parameter, parameter.getId());

        verify(reviews, times(1)).findById(existing.getId());
        verify(reviews, times(1)).save(Mockito.any(Review.class));

        assertEquals(parameter.getTitle(), result.getTitle());
        assertEquals(parameter.getBody(), result.getBody());
        assertEquals(parameter.getRating(), result.getRating());

        assertTrue(existing.getReviewed().compareTo(result.getReviewed()) <= 0);
    }

    @Test
    void deleteReview() {
        final long reviewId = 1234L;
        doNothing().when(reviews).deleteById(reviewId);

        service.deleteReview(reviewId);

        verify(reviews, times(1)).deleteById(reviewId);
    }


    private void assertEqual(Review expected, ReviewDto result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getBody(), result.getBody());
        assertEquals(expected.getMovieId(), result.getMovieId());
        assertEquals(expected.getReviewed(), result.getReviewed());
    }
}