package com.daugherty.movie.collector.controller;

import com.daugherty.movie.collector.dto.DtoConverter;
import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.themoviedb.api.MovieDbService;
import org.themoviedb.api.dto.TmdbMovie;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.daugherty.movie.collector.model.ReviewUpdater.apply;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class CollectionController {

    private final DtoConverter dtoConverter = new DtoConverter();

    private final Movies movies;
    private final Reviews reviews;
    private final MovieDbService movieDbService;

    @Operation(summary = "Get the full list of movies")
    @GetMapping(value = "/movies", produces = {"application/hal+json"})
    public CollectionModel<MovieDto> getAllMovies() {
        return CollectionModel.of(movies.findAll().stream()
                        .map(dtoConverter::toDto)
                        .map(CollectionController::addLinks)
                        .collect(Collectors.toList()),
                linkTo(methodOn(CollectionController.class).getAllMovies()).withSelfRel());
    }

    @Operation(summary = "Get a movie by its id")
    @GetMapping(value = "/movies/{id}", produces = {"application/hal+json"})
    public MovieDto getMovieById(@PathVariable long id) {
        return movies.findById(id)
                .map(dtoConverter::toDto)
                .map(CollectionController::addLinks)
                .orElseThrow(MovieNotFoundException::new);
    }

    @Operation(summary = "Add a new movie")
    @PostMapping(value = "/movies", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDto addNewMovie(@Valid @RequestBody MovieDto movieDto) {
        Movie movie = movies.save(dtoConverter.toModel(movieDto));
        return addLinks(dtoConverter.toDto(movie));
    }

    @Operation(summary = "Delete the movie with the given id")
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable long id) {
        movies.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get the full list of movie reviews")
    @GetMapping(value = "/reviews", produces = {"application/hal+json"})
    public CollectionModel<ReviewDto> getAllReviews() {
        return CollectionModel.of(reviews.findAll().stream()
                        .map(dtoConverter::toDto)
                        .map(CollectionController::addLinks)
                        .collect(Collectors.toList()),
                linkTo(methodOn(CollectionController.class).getAllReviews()).withSelfRel());
    }

    @Operation(summary = "Get the list of movie reviews for a given movie")
    @GetMapping(value = "/movies/{movieId}/reviews", produces = {"application/hal+json"})
    public CollectionModel<ReviewDto> findReviewsByMovieId(@PathVariable long movieId) {
        return CollectionModel.of(reviews.findByMovieId(movieId).stream()
                        .map(dtoConverter::toDto)
                        .map(CollectionController::addLinks)
                        .collect(Collectors.toList()),
                linkTo(methodOn(CollectionController.class).getAllReviews()).withSelfRel());
    }

    @Operation(summary = "Get the full details of the given movie")
    @GetMapping(value = "/movies/{movieId}/details", produces = {"application/hal+json"})
    public MovieDetailsDto getMovieDetails(@PathVariable long movieId) {
        Movie movie = movies.findById(movieId).orElseThrow(MovieNotFoundException::new);
        TmdbMovie tmdbMovie = movieDbService.getMovieById(movie.getTmdbId()).orElseThrow(MovieNotFoundException::new);
        List<Review> reviewList = reviews.findByMovieId(movieId);
        return dtoConverter.toDetailsDto(movie, reviewList, tmdbMovie);
    }

    // TODO: Remove the Review parameter to addNewReview and replace it with explicit parameters.
    @Operation(summary = "Add a new movie review")
    @PostMapping(value = "/reviews", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto addNewReview(@Valid @RequestBody ReviewDto reviewDto) {
        if (!movies.existsById(reviewDto.getMovieId())) {
            throw new MovieNotFoundException();
        }
        Review review = reviews.save(dtoConverter.toModel(reviewDto));
        return addLinks(dtoConverter.toDto(review));
    }

    @Operation(summary = "Get a review by its id")
    @GetMapping(value = "/reviews/{id}", produces = {"application/hal+json"})
    public ReviewDto getReviewById(@PathVariable long id) {
        return reviews.findById(id)
                .map(dtoConverter::toDto)
                .map(CollectionController::addLinks)
                .orElseThrow(ReviewNotFoundException::new);
    }

    // TODO: Remove the Review parameter to updateReview and replace it with explicit parameters.
    @Operation(summary = "Update the details of a review")
    @PutMapping(value = "/reviews/{id}", produces = {"application/hal+json"})
    public ReviewDto updateReview(@Valid @RequestBody ReviewDto updated,
                               @PathVariable long id) {
        return reviews.findById(id)
                .map(existing -> reviews.save(apply(updated, existing)))
                .map(dtoConverter::toDto)
                .map(CollectionController::addLinks)
                .orElseThrow(ReviewNotFoundException::new);
    }

    @Operation(summary = "Delete the review with the given id")
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable long id) {
        reviews.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private static MovieDto addLinks(MovieDto movie) {
        movie.add(linkTo(methodOn(CollectionController.class).getMovieById(movie.getId())).withSelfRel());
        return movie;
    }

    private static ReviewDto addLinks(ReviewDto review) {
        review.add(linkTo(methodOn(CollectionController.class).getReviewById(review.getId())).withSelfRel());
        return review;
    }
}
