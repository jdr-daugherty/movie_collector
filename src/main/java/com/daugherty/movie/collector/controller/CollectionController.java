package com.daugherty.movie.collector.controller;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

import static com.daugherty.movie.collector.model.ReviewUpdater.apply;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CollectionController {

    private final Movies movies;
    private final Reviews reviews;

    public CollectionController(Movies movies, Reviews reviews) {
        this.movies = movies;
        this.reviews = reviews;
    }

    @Operation(summary = "Get the full list of movies")
    @GetMapping(value = "/movies", produces = {"application/hal+json"})
    public CollectionModel<Movie> getAllMovies() {
        return CollectionModel.of(movies.findAll().stream()
                        .map(CollectionController::addLinks)
                        .collect(Collectors.toList()),
                linkTo(methodOn(CollectionController.class).getAllMovies()).withSelfRel());
    }

    @Operation(summary = "Get a movie by its id")
    @GetMapping(value = "/movies/{id}", produces = {"application/hal+json"})
    public Movie getMovieById(@PathVariable long id) {
        return movies.findById(id)
                .map(CollectionController::addLinks)
                .orElseThrow(MovieNotFoundException::new);
    }

    @Operation(summary = "Add a new movie")
    @PostMapping(value = "/movies", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addNewMovie(@Valid @RequestBody Movie movie) {
        return addLinks(movies.save(movie));
    }

    @Operation(summary = "Delete the movie with the given id")
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable long id) {
        movies.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get the full list of movie reviews")
    @GetMapping(value = "/reviews", produces = {"application/hal+json"})
    public CollectionModel<Review> getAllReviews() {
        return CollectionModel.of(reviews.findAll().stream()
                        .map(CollectionController::addLinks)
                        .collect(Collectors.toList()),
                linkTo(methodOn(CollectionController.class).getAllReviews()).withSelfRel());
    }

    @Operation(summary = "Get the list of movie reviews for a given movie")
    @GetMapping(value = "/movies/{movieId}/reviews", produces = {"application/hal+json"})
    public CollectionModel<Review> findReviewsByMovieId(@PathVariable long movieId) {
        return CollectionModel.of(reviews.findByMovieId(movieId).stream()
                        .map(CollectionController::addLinks)
                        .collect(Collectors.toList()),
                linkTo(methodOn(CollectionController.class).getAllReviews()).withSelfRel());
    }

    // TODO: Remove the Review parameter to addNewReview and replace it with explicit parameters.
    @Operation(summary = "Add a new movie review")
    @PostMapping(value = "/reviews", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Review addNewReview(@Valid @RequestBody Review review) {
        if (!movies.existsById(review.getMovieId())) {
            throw new MovieNotFoundException();
        }
        return addLinks(reviews.save(review));
    }

    @Operation(summary = "Get a review by its id")
    @GetMapping(value = "/reviews/{id}", produces = {"application/hal+json"})
    public Review getReviewById(@PathVariable long id) {
        return reviews.findById(id)
                .map(CollectionController::addLinks)
                .orElseThrow(ReviewNotFoundException::new);
    }

    // TODO: Remove the Review parameter to updateReview and replace it with explicit parameters.
    @Operation(summary = "Update the details of a review")
    @PutMapping(value = "/reviews/{id}", produces = {"application/hal+json"})
    public Review updateReview(@Valid @RequestBody Review updated,
                               @PathVariable long id) {
        return reviews.findById(id)
                .map(existing -> reviews.save(apply(updated, existing)))
                .map(CollectionController::addLinks)
                .orElseThrow(ReviewNotFoundException::new);
    }

    @Operation(summary = "Delete the review with the given id")
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable long id) {
        reviews.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private static Movie addLinks(Movie movie) {
        movie.add(linkTo(methodOn(CollectionController.class).getMovieById(movie.getId())).withSelfRel());
        return movie;
    }

    private static Review addLinks(Review review) {
        review.add(linkTo(methodOn(CollectionController.class).getReviewById(review.getId())).withSelfRel());
        return review;
    }
}
