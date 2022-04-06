package com.daugherty.movie.collector.service;

import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.dto.ReviewValue;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.details.MovieDetails;
import com.daugherty.movie.collector.details.MovieDetailsService;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private final ModelMapper modelMapper = new ModelMapper();

    private final Movies movies;
    private final Reviews reviews;
    private final MovieDetailsService detailsService;

    public List<MovieDto> getAllMovies() {
        return movies.findAll().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());
    }

    public MovieDto getMovieById(@PathVariable long id) {
        return movies.findById(id)
                .map(this::toDto)
                .orElseThrow(MovieNotFoundException::new);
    }

    public MovieDto addNewMovie(@Valid @RequestBody MovieDto movieDto) {
        return toDto(movies.save(this.toModel(movieDto)));
    }

    public ResponseEntity<Void> deleteMovie(@PathVariable long id) {
        movies.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public MovieDetailsDto getMovieDetails(@PathVariable long movieId) {
        Movie movie = movies.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);

        MovieDetails details = detailsService.getMovieById(movie.getTmdbId())
                .orElseThrow(MovieNotFoundException::new);

        List<Review> reviewList = reviews.findByMovieId(movieId);
        return toDetailsDto(movie, reviewList, details);
    }

    private MovieDto toDto(Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }

    private Movie toModel(MovieDto dto) {
        return modelMapper.map(dto, Movie.class);
    }

    private MovieDetailsDto toDetailsDto(Movie movie,
                                         List<Review> reviews,
                                         MovieDetails details) {
        MovieDetailsDto dto = new MovieDetailsDto();

        modelMapper.map(details, dto);
        modelMapper.map(movie, dto);

        dto.setReviews(reviews.stream()
                .map(this::toDtoValue)
                .collect(Collectors.toUnmodifiableList()));

        return dto;
    }

    private ReviewValue toDtoValue(Review review) {
        return modelMapper.map(review, ReviewValue.class);
    }
}
