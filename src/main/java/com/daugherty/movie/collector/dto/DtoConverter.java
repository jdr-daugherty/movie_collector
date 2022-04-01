package com.daugherty.movie.collector.dto;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import org.modelmapper.ModelMapper;

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

}
