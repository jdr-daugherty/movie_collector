package com.daugherty.movie.collector.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.themoviedb.api.MovieDbService;
import org.themoviedb.api.dto.TmdbMovie;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MovieDbServiceTests {

    // TODO: Add WireMock versions of these tests.

    @Autowired
    private MovieDbService movieDbService;

    @Test
    public void getReservoirDogsById() {
        Optional<TmdbMovie> result = movieDbService.getMovieById(500);
        assertTrue(result.isPresent());
        assertEquals(500, result.get().getId());
        assertEquals("Reservoir Dogs", result.get().getTitle());
    }

    @Test
    public void findReservoirDogsByQuery() {
        List<TmdbMovie> list = movieDbService.findMovies("Reservoir Dogs");
        assertTrue(list.stream().anyMatch(m -> m.getId() == 500));
    }

    @Test
    public void findReservoirDogsByTitle() {
        Optional<TmdbMovie> result = movieDbService.getMovieByTitle("Reservoir Dogs");
        assertTrue(result.isPresent());
        assertEquals(500, result.get().getId());
    }
}
