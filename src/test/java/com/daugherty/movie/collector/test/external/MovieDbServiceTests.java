package com.daugherty.movie.collector.test.external;

import com.daugherty.movie.collector.details.MovieDetails;
import com.daugherty.movie.collector.details.themoviedb.TmdbMovieDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MovieDbServiceTests {

    // TODO: Add WireMock versions of these tests.

    @Autowired
    private TmdbMovieDetailsService movieDbService;

    @Test
    public void getReservoirDogsById() {
        var result = movieDbService.getMovieById(500);
        assertEquals(500, result.map(MovieDetails::getId).orElse(0L));
        assertEquals("Reservoir Dogs",
                result.map(MovieDetails::getTitle).orElse("Failure"));
    }

    @Test
    public void findReservoirDogsByQuery() {
        var list = movieDbService.findMovies("Reservoir Dogs");
        assertTrue(list.stream().anyMatch(m -> m.getId() == 500));
    }

    @Test
    public void findReservoirDogsByTitle() {
        var result = movieDbService.getMovieByTitle("Reservoir Dogs");
        assertTrue(result.isPresent());
        assertEquals("Reservoir Dogs",
                result.map(MovieDetails::getTitle).orElse("Failure"));
    }
}
