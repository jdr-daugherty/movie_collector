package com.daugherty.movie.collector.controller;

import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.service.MoviesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MoviesController {

    private final MoviesService moviesService;

    @Operation(summary = "Get the full list of movies")
    @GetMapping(value = "/movies")
    public List<MovieDto> getAllMovies() {
        return moviesService.getAllMovies();
    }

    @Operation(summary = "Get a movie by its id")
    @GetMapping(value = "/movies/{id}")
    public MovieDto getMovieById(@PathVariable long id) {
        return moviesService.getMovieById(id);
    }

    @Operation(summary = "Add a new movie")
    @PostMapping(value = "/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDto addNewMovie(@Valid @RequestBody MovieDto movieDto) {
        return moviesService.addNewMovie(movieDto);
    }

    @Operation(summary = "Delete the movie with the given id")
    @DeleteMapping("/movies/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable long id) {
        moviesService.deleteMovie(id);
    }

    @Operation(summary = "Get the full details of the given movie")
    @GetMapping(value = "/movies/{movieId}/details")
    public MovieDetailsDto getMovieDetails(@PathVariable long movieId) {
        return moviesService.getMovieDetails(movieId);
    }
}
