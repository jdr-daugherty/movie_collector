package com.daugherty.movie.collector.service;

import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface MoviesService {
    List<MovieDto> getAllMovies();

    MovieDto getMovieById(long id);

    MovieDto addNewMovie(MovieDto movieDto);

    void deleteMovie(long id);

    MovieDetailsDto getMovieDetails(long movieId);
}
