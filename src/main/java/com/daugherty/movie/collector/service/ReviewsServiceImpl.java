package com.daugherty.movie.collector.service;

import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.exception.ReviewNotFoundException;
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
public class ReviewsServiceImpl implements ReviewsService {

    private final ModelMapper modelMapper = new ModelMapper();

    private final Movies movies;
    private final Reviews reviews;

    @Override
    public List<ReviewDto> getAllReviews() {
        return reviews.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto addNewReview(ReviewDto reviewDto) throws MovieNotFoundException {
        if (!movies.existsById(reviewDto.getMovieId())) {
            throw new MovieNotFoundException();
        }
        return this.toDto(reviews.save(this.toModel(reviewDto)));
    }

    @Override
    public ReviewDto getReviewById(long id) throws ReviewNotFoundException {
        return reviews.findById(id)
                .map(this::toDto)
                .orElseThrow(ReviewNotFoundException::new);
    }

    @Override
    public ReviewDto updateReview(ReviewDto source, long id) throws ReviewNotFoundException {
        return reviews.findById(id)
                .map(existing -> reviews.save(applyReviewUpdates(source, existing)))
                .map(this::toDto)
                .orElseThrow(ReviewNotFoundException::new);
    }

    @Override
    public void deleteReview(long id) {
        reviews.deleteById(id);
    }

    @Override
    public List<ReviewDto> findReviewsByMovieId(long movieId) {
        return reviews.findByMovieId(movieId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    private ReviewDto toDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

    private Review toModel(ReviewDto dto) {
        return modelMapper.map(dto, Review.class);
    }

    private static Review applyReviewUpdates(ReviewDto source, Review target) {
        if (source.getTitle() != null) {
            target.setTitle(source.getTitle());
        }
        if (source.getBody() != null) {
            target.setBody(source.getBody());
        }
        if (source.getReviewed() != null && source.getReviewed().after(target.getReviewed())) {
            target.setReviewed(source.getReviewed());
        }
        if (source.getRating() != null) {
            target.setRating(source.getRating());
        }
        return target;
    }
}
