package com.daugherty.movie.collector.service;

import com.daugherty.movie.collector.details.MovieDetails;
import com.daugherty.movie.collector.details.MovieDetailsService;
import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.dto.ReviewValue;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoviesServiceImpl implements MoviesService {

    private final ModelMapper modelMapper = new ModelMapper();

    private final Movies movies;
    private final Reviews reviews;
    private final MovieDetailsService detailsService;

    @Override
    public List<MovieDto> getAllMovies() {
        return movies.findAll().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());
    }

    @Override
    public MovieDto getMovieById(long id) throws MovieNotFoundException {
        return movies.findById(id)
                .map(this::toDto)
                .orElseThrow(MovieNotFoundException::new);
    }

    @Override
    public MovieDto addNewMovie(MovieDto movieDto) {
        return toDto(movies.save(this.toModel(movieDto)));
    }

    @Override
    public void deleteMovie(long id) {
        movies.deleteById(id);
    }

    @Override
    public MovieDetailsDto getMovieDetails(long movieId) {
        Movie movie = movies.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);

        MovieDetails details = detailsService.getMovieById(movie.getDetailsId())
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
