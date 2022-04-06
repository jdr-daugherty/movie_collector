package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.controller.MoviesController;
import com.daugherty.movie.collector.dto.DtoConverter;
import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
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

import java.util.List;
import java.util.Optional;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(SpringExtension.class)
class MoviesServiceTests {

    private final DtoConverter converter = new DtoConverter();

    @MockBean
    private Movies movies;

    @MockBean
    private Reviews reviews;

    @MockBean
    private MovieDbService movieDbService;

    private MoviesController controller;

    @BeforeEach
    private void setupController() {
        controller = new MoviesController(movies, reviews, movieDbService);
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
}