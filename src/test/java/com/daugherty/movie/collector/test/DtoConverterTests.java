package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.dto.DtoConverter;
import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import org.junit.jupiter.api.Test;
import org.themoviedb.api.dto.TmdbMovie;

import java.util.List;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoConverterTests {

    private static final DtoConverter dtoConverter = new DtoConverter();

    @Test
    void reviewToDto() {
        Review expected = reviewWithId();
        ReviewDto result = dtoConverter.toDto(expected);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getBody(), result.getBody());
        assertEquals(expected.getMovieId(), result.getMovieId());
        assertEquals(expected.getReviewed(), result.getReviewed());
    }

    @Test
    void reviewToModel() {
        Review expected = reviewWithId();
        ReviewDto intermediate = dtoConverter.toDto(expected);
        Review result = dtoConverter.toModel(intermediate);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getBody(), result.getBody());
        assertEquals(expected.getMovieId(), result.getMovieId());
        assertEquals(expected.getReviewed(), result.getReviewed());
    }

    @Test
    void movieToDto() {
        Movie expected = movieWithId();
        MovieDto result = dtoConverter.toDto(expected);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getTmdbId(), result.getTmdbId());
    }

    @Test
    void movieToModel() {
        Movie expected = movieWithId();
        MovieDto intermediate = dtoConverter.toDto(expected);
        Movie result = dtoConverter.toModel(intermediate);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getTmdbId(), result.getTmdbId());
    }

    @Test
    void testDetailsDto() {
        TmdbMovie tmdbMovie = tmdbMovie();

        Movie movie = movieWithId();
        movie.setTmdbId(tmdbMovie.getId());
        movie.setTitle(tmdbMovie.getTitle());

        Review review = reviewWithId();
        review.setMovieId(movie.getId());

        MovieDetailsDto dto = dtoConverter.toDetailsDto(movie, List.of(review), tmdbMovie);

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
}
