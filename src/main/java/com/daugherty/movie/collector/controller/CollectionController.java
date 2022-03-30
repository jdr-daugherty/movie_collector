package com.daugherty.movie.collector.controller;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
public class CollectionController {

    private final Movies movies;
    private final Reviews reviews;

    public CollectionController(Movies movies, Reviews reviews) {
        this.movies = movies;
        this.reviews = reviews;
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return movies.findAll();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable long id) {
        return movies.findById(id)
                .map(m -> ResponseEntity.ok().location(URI.create("/movies/" + id)).body(m))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/movies")
    public ResponseEntity<Movie> addNewMovie(@Valid @RequestBody Movie movie) {
        Movie saved = movies.save(movie);
        return ResponseEntity.created(URI.create("/movies/" + movie.getId()))
                .body(saved);
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return reviews.findAll();
    }

    @PostMapping("/reviews")
    public ResponseEntity<Review> addNewReview(@Valid @RequestBody Review review) {
        Review saved = reviews.save(review);
        return ResponseEntity.created(URI.create("/reviews/" + review.getId()))
                .body(saved);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable long id) {
        return reviews.findById(id)
                .map(m -> ResponseEntity.ok().location(URI.create("/reviews/" + id)).body(m))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<Review> updateReview(@Valid @RequestBody Review updated) {
        return reviews.findById(updated.getId())
                .map(existing -> patchReview(updated, existing))
                .map(existing -> ResponseEntity.ok().location(URI.create("/reviews/" + updated.getId())).body(existing))
                .orElse(ResponseEntity.notFound().build());
    }

    private Review patchReview(Review source, Review target){
        if( source.getTitle() != null ) {
            target.setTitle(source.getTitle());
        }
        if( source.getBody() != null ) {
            target.setBody(source.getBody());
        }
        if( source.getReviewed() != null && source.getReviewed().after(target.getReviewed())) {
            target.setReviewed(source.getReviewed());
        }
        if( source.getRating() != null ) {
            target.setRating(source.getRating());
        }
        return target;
    }
}
