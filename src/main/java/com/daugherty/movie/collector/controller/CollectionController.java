package com.daugherty.movie.collector.controller;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
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

    @GetMapping(value = "/movies", produces = {"application/hal+json"})
    public CollectionModel<Movie> getAllMovies() {
        return CollectionModel.of(movies.findAll().stream()
                        .map(CollectionController::addLinks)
                        .collect(Collectors.toList()),
                linkTo(methodOn(CollectionController.class).getAllMovies()).withSelfRel());
    }

    @GetMapping(value = "/movies/{id}", produces = {"application/hal+json"})
    public Movie getMovieById(@PathVariable long id) {
        return movies.findById(id)
                .map(CollectionController::addLinks)
                .orElseThrow(MovieNotFoundException::new);
    }

    @PostMapping(value = "/movies", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addNewMovie(@Valid @RequestBody Movie movie) {
        return addLinks(movies.save(movie));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable long id) {
        movies.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/reviews", produces = {"application/hal+json"})
    public CollectionModel<Review> getAllReviews() {
        return CollectionModel.of(reviews.findAll().stream()
                        .map(CollectionController::addLinks)
                        .collect(Collectors.toList()),
                linkTo(methodOn(CollectionController.class).getAllReviews()).withSelfRel());
    }

    @PostMapping(value = "/reviews", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Review addNewReview(@Valid @RequestBody Review review) {
        if (!movies.existsById(review.getMovieId())) {
            throw new MovieNotFoundException();
        }
        return addLinks(reviews.save(review));
    }

    @GetMapping(value = "/reviews/{id}", produces = {"application/hal+json"})
    public Review getReviewById(@PathVariable long id) {
        return reviews.findById(id)
                .map(CollectionController::addLinks)
                .orElseThrow(ReviewNotFoundException::new);
    }

    @PutMapping(value = "/reviews/{id}", produces = {"application/hal+json"})
    public Review updateReview(@Valid @RequestBody Review updated,
                               @PathVariable long id) {
        return reviews.findById(id)
                .map(existing -> reviews.save(apply(updated, existing)))
                .map(CollectionController::addLinks)
                .orElseThrow(ReviewNotFoundException::new);
    }

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
