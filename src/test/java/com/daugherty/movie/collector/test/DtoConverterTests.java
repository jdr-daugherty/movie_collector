package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.dto.DtoConverter;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.daugherty.movie.collector.test.TestObjectMother.movieWithId;
import static com.daugherty.movie.collector.test.TestObjectMother.reviewWithId;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class DtoConverterTests {

    @Bean
    private DtoConverter dtoConverter() {
        return new DtoConverter();
    }

    @Test
    void reviewToDto() {
        Review expected = reviewWithId();
        ReviewDto result = dtoConverter().toDto(expected);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getBody(), result.getBody());
        assertEquals(expected.getMovieId(), result.getMovieId());
        assertEquals(expected.getReviewed(), result.getReviewed());
    }

    @Test
    void reviewToModel() {
        Review expected = reviewWithId();
        ReviewDto intermediate = dtoConverter().toDto(expected);
        Review result = dtoConverter().toModel(intermediate);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getBody(), result.getBody());
        assertEquals(expected.getMovieId(), result.getMovieId());
        assertEquals(expected.getReviewed(), result.getReviewed());
    }

    @Test
    void movieToDto() {
        Movie expected = movieWithId();
        MovieDto result = dtoConverter().toDto(expected);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getTmdbId(), result.getTmdbId());
    }

    @Test
    void movieToModel() {
        Movie expected = movieWithId();
        MovieDto intermediate = dtoConverter().toDto(expected);
        Movie result = dtoConverter().toModel(intermediate);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getTmdbId(), result.getTmdbId());
    }
}
