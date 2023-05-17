package com.daugherty.movie.collector.test.service;

import com.daugherty.movie.collector.details.MovieDetails;
import com.daugherty.movie.collector.details.MovieDetailsService;
import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import com.daugherty.movie.collector.service.MoviesServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.daugherty.movie.collector.test.factory.MovieFactory.*;
import static com.daugherty.movie.collector.test.factory.ReviewFactory.reviewWithId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoviesServiceTests {

    @Mock
    private Movies movies;

    @Mock
    private Reviews reviews;

    @Mock
    private MovieDetailsService detailsService;

    @InjectMocks
    private MoviesServiceImpl service;

    @Test
    void getAllMoviesWithNoMovies() {
        assertTrue(service.getAllMovies().isEmpty());
    }

    @Test
    void getAllMovies() {
        List<Movie> expected = List.of(movieWithId());
        when(movies.findAll()).thenReturn(expected);

        List<MovieDto> result = service.getAllMovies();

        verify(movies, times(1)).findAll();

        assertEqual(expected.get(0), result.get(0));
    }

    @Test
    void getMovieById() {
        Movie expected = movieWithId();
        when(movies.findById(expected.getId())).thenReturn(Optional.of(expected));

        MovieDto result = service.getMovieById(expected.getId());

        verify(movies, times(1)).findById(expected.getId());

        assertEqual(expected, result);
    }

    @Test
    void getMovieDetails() {
        MovieDetails movieDetails = movieDetails();

        Movie movie = movieWithId();
        movie.setDetailsId(movieDetails.getId());
        movie.setTitle(movieDetails.getTitle());

        Review review = reviewWithId();
        review.setMovieId(movie.getId());

        when(movies.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(reviews.findByMovieId(movie.getId())).thenReturn(List.of(review));
        when(detailsService.getMovieById(movie.getDetailsId())).thenReturn(Optional.of(movieDetails));

        MovieDetailsDto dto = service.getMovieDetails(movie.getId());

        verify(movies, times(1)).findById(movie.getId());
        verify(reviews, times(1)).findByMovieId(movie.getId());
        verify(detailsService, times(1)).getMovieById(movie.getDetailsId());

        assertEqual(movie, dto);
        assertEqual(movieDetails, dto);
        assertEqual(review, dto);
    }

    @Test
    void getMovieByIdNotFound() {
        Assertions.assertThrows(MovieNotFoundException.class,
                () -> service.getMovieById(Long.MIN_VALUE));
    }

    @Test
    void addNewMovie() {
        MovieDto expected = movieDtoWithoutId();
        when(movies.save(Mockito.any(Movie.class))).then(returnsFirstArg());

        MovieDto result = service.addNewMovie(expected);
        verify(movies, times(1)).save(any(Movie.class));

        assertEquals(expected, result);
    }

    @Test
    void deleteMovie() {
        final long movieId = 1234L;
        doNothing().when(movies).deleteById(movieId);

        service.deleteMovie(movieId);

        verify(movies, times(1)).deleteById(movieId);
    }

    private void assertEqual(Movie expected, MovieDto result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getDetailsId(), result.getDetailsId());
    }

    private void assertEqual(Movie movie, MovieDetailsDto dto) {
        assertEquals(movie.getId(), dto.getId());
        assertEquals(movie.getTitle(), dto.getTitle());
    }

    private void assertEqual(MovieDetails movieDetails, MovieDetailsDto dto) {
        assertEquals(movieDetails.getTitle(), dto.getTitle());
        assertEquals(movieDetails.getTagline(), dto.getTagline());
        assertEquals(movieDetails.isAdult(), dto.isAdult());
        assertEquals(movieDetails.getReleaseDate(), dto.getReleaseDate());
        assertEquals(movieDetails.getVoteAverage(), dto.getVoteAverage());
        assertEquals(movieDetails.getRuntime(), dto.getRuntime());
    }

    private void assertEqual(Review review, MovieDetailsDto dto) {
        assertEquals(1, dto.getReviews().size());
        assertEquals(review.getTitle(), dto.getReviews().get(0).getTitle());
        assertEquals(review.getBody(), dto.getReviews().get(0).getBody());
        assertEquals(review.getReviewed(), dto.getReviews().get(0).getReviewed());
        assertEquals(review.getRating(), dto.getReviews().get(0).getRating());
    }
}