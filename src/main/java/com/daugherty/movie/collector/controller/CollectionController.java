package com.daugherty.movie.collector.controller;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.daugherty.movie.collector.model.ReviewUpdater.apply;

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
    public Movie getMovieById(@PathVariable long id) {
        return movies.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    @PostMapping("/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addNewMovie(@Valid @RequestBody Movie movie) {
        return movies.save(movie);
    }

    @DeleteMapping("/movies/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMovie(@PathVariable long id) {
        movies.deleteById(id);
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return reviews.findAll();
    }

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Review addNewReview(@Valid @RequestBody Review review) {
        if (!movies.existsById(review.getMovieId())) {
            throw new MovieNotFoundException();
        }
        return reviews.save(review);
    }

    @GetMapping("/reviews/{id}")
    public Review getReviewById(@PathVariable long id) {
        return reviews.findById(id).orElseThrow(ReviewNotFoundException::new);
    }

    @PutMapping("/reviews/{id}")
    public Review updateReview(@Valid @RequestBody Review updated,
                               @PathVariable long id) {
        return reviews.findById(id)
                .map(existing -> reviews.save(apply(updated, existing)))
                .orElseThrow(ReviewNotFoundException::new);
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable long id) {
        reviews.deleteById(id);
    }
}
