package com.daugherty.movie.collector.controller;

import com.daugherty.movie.collector.dto.DtoConverter;
import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.exception.ReviewNotFoundException;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.themoviedb.api.MovieDbService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.daugherty.movie.collector.model.ReviewUpdater.apply;

@RestController
@RequiredArgsConstructor
public class ReviewsController {

    private final DtoConverter dtoConverter = new DtoConverter();

    private final Movies movies;
    private final Reviews reviews;
    private final MovieDbService movieDbService;

    @Operation(summary = "Get the full list of movie reviews")
    @GetMapping(value = "/reviews")
    public List<ReviewDto> getAllReviews() {
        return reviews.findAll().stream()
                .map(dtoConverter::toDto)
                .collect(Collectors.toList());
    }

    // TODO: Remove the Review parameter to addNewReview and replace it with explicit parameters.
    @Operation(summary = "Add a new movie review")
    @PostMapping(value = "/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto addNewReview(@Valid @RequestBody ReviewDto reviewDto) {
        if (!movies.existsById(reviewDto.getMovieId())) {
            throw new MovieNotFoundException();
        }
        return dtoConverter.toDto(reviews.save(dtoConverter.toModel(reviewDto)));
    }

    @Operation(summary = "Get a review by its id")
    @GetMapping(value = "/reviews/{id}")
    public ReviewDto getReviewById(@PathVariable long id) {
        return reviews.findById(id)
                .map(dtoConverter::toDto)
                .orElseThrow(ReviewNotFoundException::new);
    }

    // TODO: Remove the Review parameter to updateReview and replace it with explicit parameters.
    @Operation(summary = "Update the details of a review")
    @PutMapping(value = "/reviews/{id}")
    public ReviewDto updateReview(@Valid @RequestBody ReviewDto updated,
                                  @PathVariable long id) {
        return reviews.findById(id)
                .map(existing -> reviews.save(apply(updated, existing)))
                .map(dtoConverter::toDto)
                .orElseThrow(ReviewNotFoundException::new);
    }

    @Operation(summary = "Delete the review with the given id")
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable long id) {
        reviews.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get the list of movie reviews for a given movie")
    @GetMapping(value = "/movies/{movieId}/reviews")
    public List<ReviewDto> findReviewsByMovieId(@PathVariable long movieId) {
        return reviews.findByMovieId(movieId).stream()
                .map(dtoConverter::toDto)
                .collect(Collectors.toList());
    }
}
