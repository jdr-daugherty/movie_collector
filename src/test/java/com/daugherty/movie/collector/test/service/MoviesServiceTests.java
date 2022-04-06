package com.daugherty.movie.collector.test.service;

import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import com.daugherty.movie.collector.service.MoviesService;
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

import java.util.List;
import java.util.Optional;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(SpringExtension.class)
class MoviesServiceTests {

    @MockBean
    private Movies movies;

    @MockBean
    private Reviews reviews;

    @MockBean
    private MovieDbService movieDbService;

    private MoviesService service;

    @BeforeEach
    private void setup() {
        service = new MoviesService(movies, reviews, movieDbService);
    }

    @Test
    void getAllMoviesWithNoMovies() {
        assertTrue(service.getAllMovies().isEmpty());
    }

    @Test
    void getAllMovies() {
        List<Movie> expected = List.of(movieWithId());
        when(movies.findAll()).thenReturn(expected);

        List<MovieDto> allMovies = service.getAllMovies();

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

        MovieDto result = service.getMovieById(expected.getId());

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

        MovieDetailsDto dto = service.getMovieDetails(movie.getId());

        verify(movies, times(1)).findById(movie.getId());
        verify(reviews, times(1)).findByMovieId(movie.getId());
        verify(movieDbService, times(1)).getMovieById(movie.getTmdbId());

        // This logic ensures that the details DTO includes all the expected values.
        // Note that this test cannot detect missing or untested DTO fields.
        assertEquals(movie.getId(), dto.getId());
        assertEquals(movie.getTitle(), dto.getTitle());

        assertEquals(tmdbMovie.getTitle(), dto.getTitle());
        assertEquals(tmdbMovie.getTagline(), dto.getTagline());
        assertEquals(tmdbMovie.isAdult(), dto.isAdult());
        assertEquals(tmdbMovie.getReleaseDate(), dto.getReleaseDate());
        assertEquals(tmdbMovie.getVoteAverage(), dto.getVoteAverage());
        assertEquals(tmdbMovie.getRuntime(), dto.getRuntime());

        assertEquals(1, dto.getReviews().size());
        assertEquals(review.getTitle(), dto.getReviews().get(0).getTitle());
        assertEquals(review.getBody(), dto.getReviews().get(0).getBody());
        assertEquals(review.getReviewed(), dto.getReviews().get(0).getReviewed());
        assertEquals(review.getRating(), dto.getReviews().get(0).getRating());
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

        ResponseEntity<Void> response = service.deleteMovie(movieId);

        verify(movies, times(1)).deleteById(movieId);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}