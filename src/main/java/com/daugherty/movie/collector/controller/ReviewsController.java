package com.daugherty.movie.collector.controller;

import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.service.ReviewsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewsController {
    private final ReviewsService reviewsService;

    @Operation(summary = "Get the full list of movie reviews")
    @GetMapping(value = "/reviews")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getAllReviews() {
        return reviewsService.getAllReviews();
    }

    // TODO: Remove the Review parameter to addNewReview and replace it with explicit parameters.
    @Operation(summary = "Add a new movie review")
    @PostMapping(value = "/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto addNewReview(@Valid @RequestBody ReviewDto reviewDto) {
        return reviewsService.addNewReview(reviewDto);
    }

    @Operation(summary = "Get a review by its id")
    @GetMapping(value = "/reviews/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto getReviewById(@PathVariable long id) {
        return reviewsService.getReviewById(id);
    }

    // TODO: Remove the Review parameter to updateReview and replace it with explicit parameters.
    @Operation(summary = "Update the details of a review")
    @PutMapping(value = "/reviews/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ReviewDto updateReview(@Valid @RequestBody ReviewDto updated,
                                  @PathVariable long id) {
        return reviewsService.updateReview(updated, id);
    }

    @Operation(summary = "Delete the review with the given id")
    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable long id) {
        reviewsService.deleteReview(id);
    }

    @Operation(summary = "Get the list of movie reviews for a given movie")
    @GetMapping(value = "/reviews/by_movie/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> findReviewsByMovieId(@PathVariable long movieId) {
        return reviewsService.findReviewsByMovieId(movieId);
    }
}
