package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.controller.CollectionController;
import com.daugherty.movie.collector.controller.MovieNotFoundException;
import com.daugherty.movie.collector.controller.ReviewNotFoundException;
import com.daugherty.movie.collector.dto.DtoConverter;
import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.themoviedb.api.MovieDbService;
import org.themoviedb.api.dto.TmdbMovie;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(SpringExtension.class)
class CollectionControllerTests {

    private final DtoConverter converter = new DtoConverter();

    @MockBean
    private Movies movies;

    @MockBean
    private Reviews reviews;

    @MockBean
    private MovieDbService movieDbService;

    private CollectionController controller;

    @BeforeEach
    private void setupController() {
        controller = new CollectionController(movies, reviews, movieDbService);
    }

    @Test
    void getAllMoviesWithNoMovies() {
        assertTrue(controller.getAllMovies().isEmpty());
    }

    @Test
    void getAllMovies() {
        List<Movie> expected = List.of(movieWithId());
        when(movies.findAll()).thenReturn(expected);

        List<MovieDto> allMovies = controller.getAllMovies();

        verify(movies, times(1)).findAll();

        MovieDto result = allMovies.get(0);
        assertEquals(expected.get(0).getId(), result.getId());
        assertEquals(expected.get(0).getTitle(), result.getTitle());
        assertEquals(expected.get(0).getTmdbId(), result.getTmdbId());
    }

    @Test
    void getMovieById() {
        Movie expected = movieWithId();
        when(movies.findById(expected.getId())).thenReturn(Optional.of(expected));

        MovieDto result = controller.getMovieById(expected.getId());

        verify(movies, times(1)).findById(expected.getId());

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getTmdbId(), result.getTmdbId());
    }

    @Test
    void getMovieDetails() {
        TmdbMovie tmdbMovie = tmdbMovie();

        Movie movie = movieWithId();
        movie.setTmdbId(tmdbMovie.getId());
        movie.setTitle(tmdbMovie.getTitle());

        Review review = reviewWithId();
        review.setMovieId(movie.getId());

        when(movies.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(reviews.findByMovieId(movie.getId())).thenReturn(List.of(review));
        when(movieDbService.getMovieById(movie.getTmdbId())).thenReturn(Optional.of(tmdbMovie));

        MovieDetailsDto dto = controller.getMovieDetails(movie.getId());

        verify(movies, times(1)).findById(movie.getId());
        verify(reviews, times(1)).findByMovieId(movie.getId());
        verify(movieDbService, times(1)).getMovieById(movie.getTmdbId());

        // Verify that at least some of the movie, review, and TMDB data is included.
        // Full DTO conversion logic is covered in the DTO converter tests.
        assertEquals(movie.getId(), dto.getId());
        assertEquals(movie.getTitle(), dto.getTitle());
        assertEquals(tmdbMovie.getTagline(), dto.getTagline());
        assertEquals(1, dto.getReviews().size());
        assertEquals(review.getTitle(), dto.getReviews().get(0).getTitle());
    }

    @Test
    void getMovieByIdNotFound() {
        Assertions.assertThrows(MovieNotFoundException.class,
                () -> controller.getMovieById(Long.MIN_VALUE));
    }

    @Test
    void addNewMovie() {
        Movie expected = movieWithoutId();
        MovieDto expectedDto = converter.toDto(expected);
        when(movies.save(Mockito.any())).thenReturn(expected);

        MovieDto result = controller.addNewMovie(expectedDto);
        verify(movies, times(1)).save(Mockito.any());

        assertEquals(expectedDto, result);
    }

    @Test
    void deleteMovie() {
        final long movieId = 1234L;
        doNothing().when(movies).deleteById(movieId);

        ResponseEntity<Void> response = controller.deleteMovie(movieId);

        verify(movies, times(1)).deleteById(movieId);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getAllReviewsWithNoReviews() {
        assertTrue(controller.getAllReviews().isEmpty());
    }

    @Test
    void getAllReviews() {
        List<Review> expected = List.of(reviewWithId());
        when(reviews.findAll()).thenReturn(expected);

        List<ReviewDto> allReviews = controller.getAllReviews();
        verify(reviews, times(1)).findAll();

        ReviewDto result = allReviews.get(0);
        assertEquals(expected.get(0).getTitle(), result.getTitle());
    }

    @Test
    void findReviewsByMovieId() {
        List<Review> expected = List.of(reviewWithId());
        when(reviews.findByMovieId(2)).thenReturn(expected);

        List<ReviewDto> allReviews = controller.findReviewsByMovieId(2);
        verify(reviews, times(1)).findByMovieId(2);

        ReviewDto result = allReviews.get(0);
        assertEquals(expected.get(0).getTitle(), result.getTitle());
    }

    @Test
    void getReviewByIdNotFound() {
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> controller.getReviewById(Long.MIN_VALUE));
    }

    @Test
    void getReviewById() {
        Review expected = reviewWithId();
        when(reviews.findById(expected.getId())).thenReturn(Optional.of(expected));

        ReviewDto result = controller.getReviewById(expected.getId());
        verify(reviews, times(1)).findById(expected.getId());

        assertEquals(expected.getTitle(), result.getTitle());
    }

    @Test
    void addNewReview() {
        Movie movie = movieWithId();
        when(movies.existsById(movie.getId())).thenReturn(true);

        Review expected = reviewWithoutId();
        expected.setMovieId(movie.getId());
        expected.setReviewed(new Date());

        ReviewDto expectedDto = converter.toDto(expected);

        when(reviews.save(Mockito.any())).thenReturn(expected);

        ReviewDto result = controller.addNewReview(expectedDto);

        verify(movies, times(1)).existsById(movie.getId());
        verify(reviews, times(1)).save(Mockito.any());

        assertEquals(expectedDto, result);
    }

    @Test
    void addNewReviewMovieNotFound() {
        when(movies.existsById(Mockito.any())).thenReturn(false);

        final ReviewDto reviewDto = converter.toDto(reviewWithoutId());
        assertThrows(MovieNotFoundException.class, () -> controller.addNewReview(reviewDto));

        verify(movies, times(1)).existsById(Mockito.any());
    }

    @Test
    void updateReviewNotFound() {
        when(reviews.findById(Mockito.any())).thenReturn(Optional.empty());

        final ReviewDto reviewDto = converter.toDto(reviewWithId());
        assertThrows(ReviewNotFoundException.class,
                () -> controller.updateReview(reviewDto, reviewDto.getId()));

        verify(reviews, times(1)).findById(Mockito.any());
    }

    @Test
    void updateReview() {
        Review existing = reviewWithId();
        Review updated = reviewWithId();
        updated.setTitle("New Review Title!");
        updated.setBody("New Review Body");
        updated.setRating(10);

        when(reviews.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(reviews.save(Mockito.any())).thenReturn(updated);

        ReviewDto result = controller.updateReview(converter.toDto(updated), updated.getId());

        verify(reviews, times(1)).findById(existing.getId());
        verify(reviews, times(1)).save(Mockito.any());

        assertEquals(converter.toDto(updated), result);
    }

    @Test
    void deleteReview() {
        final long reviewId = 1234L;
        doNothing().when(reviews).deleteById(reviewId);

        controller.deleteReview(reviewId);

        verify(reviews, times(1)).deleteById(reviewId);
    }
}