package com.daugherty.movie.collector.dto;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import org.modelmapper.ModelMapper;
import org.themoviedb.api.dto.TmdbMovie;

import java.util.List;
import java.util.stream.Collectors;

public class DtoConverter {

    private final ModelMapper modelMapper = new ModelMapper();

    public MovieDto toDto(Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }

    public Movie toModel(MovieDto dto) {
        return modelMapper.map(dto, Movie.class);
    }

    public ReviewDto toDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

    public Review toModel(ReviewDto dto) {
        return modelMapper.map(dto, Review.class);
    }

    public MovieDetailsDto toDetailsDto(Movie movie, List<Review> reviews, TmdbMovie tmdbMovie) {
        MovieDetailsDto dto = new MovieDetailsDto();

        modelMapper.map(tmdbMovie, dto);
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
