package com.daugherty.movie.collector.test.external;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.daugherty.movie.collector.details.themoviedb.TmdbMovieDetailsService;

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
        assertTrue(result.isPresent());
        assertEquals(500, result.get().getId());
        assertEquals("Reservoir Dogs", result.get().getTitle());
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
        assertEquals(500, result.get().getId());
    }
}
