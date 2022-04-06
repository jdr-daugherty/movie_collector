package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.controller.CollectionController;
import com.daugherty.movie.collector.dto.DtoConverter;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.themoviedb.api.MovieDbService;

import java.util.Optional;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(CollectionController.class)
class CollectionEndpointTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Movies movies;

    @MockBean
    private Reviews reviews;

    @MockBean
    private MovieDbService movieDbService;

    @Autowired
    private CollectionController controller;

//    @Test
//    void getAllMovies() {
//        List<Movie> expected = List.of(movieWithId());
//        given(movies.findAll()).thenReturn(expected);
//
//        List<MovieDto> allMovies = controller.getAllMovies();
//
//        MovieDto result = allMovies.get(0);
//        assertEquals(expected.get(0).getId(), result.getId());
//        assertEquals(expected.get(0).getTitle(), result.getTitle());
//        assertEquals(expected.get(0).getTmdbId(), result.getTmdbId());
//    }
//
//    @Test
//    void getMovieById() {
//        Movie expected = movieWithId();
//        given(movies.findById(expected.getId())).thenReturn(Optional.of(expected));
//
//        MovieDto result = controller.getMovieById(expected.getId());
//
//        assertEquals(expected.getId(), result.getId());
//        assertEquals(expected.getTitle(), result.getTitle());
//        assertEquals(expected.getTmdbId(), result.getTmdbId());
//    }
//
//    @Test
//    void getMovieDetails() {
//        TmdbMovie tmdbMovie = tmdbMovie();
//
//        Movie movie = movieWithId();
//        movie.setTmdbId(tmdbMovie.getId());
//        movie.setTitle(tmdbMovie.getTitle());
//
//        Review review = reviewWithId();
//        review.setMovieId(movie.getId());
//
//        given(movies.findById(movie.getId())).thenReturn(Optional.of(movie));
//        given(reviews.findByMovieId(movie.getId())).thenReturn(List.of(review));
//        given(movieDbService.getMovieById(movie.getTmdbId())).thenReturn(Optional.of(tmdbMovie));
//
//        MovieDetailsDto dto = controller.getMovieDetails(movie.getId());
//
//        // verify movie included
//        assertEquals(movie.getId(), dto.getId());
//        assertEquals(movie.getTitle(), dto.getTitle());
//
//        // verify TMDB info included
//        assertEquals(tmdbMovie.getTagline(), dto.getTagline());
//        assertEquals(tmdbMovie.getTagline(), dto.getTagline());
//
//        // verify review included
//        assertEquals(1, dto.getReviews().size());
//        assertEquals(review.getTitle(), dto.getReviews().get(0).getTitle());
//    }
//
//    @Test
//    void getMovieByIdNotFound() {
//        Assertions.assertThrows(MovieNotFoundException.class,
//                () -> controller.getMovieById(Long.MIN_VALUE));
//    }

    @Test
    void addNewMovie() throws Exception {
        Movie expected = movieWithoutId();
        String expectedJson = toJson(expected);
        when(movies.save(Mockito.any())).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));
    }

    @Test
    void deleteMovie() throws Exception {
        when(movies.existsById(1234L)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/movies/1234"))
                .andExpect(status().isOk());
    }

//    @Test
//    void getAllReviews() {
//        List<Review> expected = List.of(reviewWithId());
//        when(reviews.findAll()).thenReturn(expected);
//
//        List<ReviewDto> allReviews = controller.getAllReviews();
//
//        ReviewDto result = allReviews.get(0);
//        assertEquals(expected.get(0).getTitle(), result.getTitle());
//    }
//
//    @Test
//    void findReviewsByMovieId() {
//        List<Review> expected = List.of(reviewWithId());
//        when(reviews.findByMovieId(2)).thenReturn(expected);
//
//        List<ReviewDto> allReviews = controller.findReviewsByMovieId(2);
//
//        ReviewDto result = allReviews.get(0);
//        assertEquals(expected.get(0).getTitle(), result.getTitle());
//    }
//
//    @Test
//    void getReviewByIdNotFound() {
//        Assertions.assertThrows(ReviewNotFoundException.class,
//                () -> controller.getReviewById(Long.MIN_VALUE));
//    }
//
//    @Test
//    void getReviewById() {
//        Review expected = reviewWithId();
//        when(reviews.findById(expected.getId())).thenReturn(Optional.of(expected));
//
//        ReviewDto result = controller.getReviewById(expected.getId());
//
//        assertEquals(expected.getTitle(), result.getTitle());
//    }

    @Test
    void addNewReview() throws Exception {
        Movie movie = movieWithId();
        when(movies.existsById(movie.getId())).thenReturn(true);

        Review expected = reviewWithoutId();
        expected.setMovieId(movie.getId());
        String expectedJson = toJson(new DtoConverter().toDto(expected));
        when(reviews.save(Mockito.any())).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));
    }

    @Test
    void addNewReviewMovieNotFound() throws Exception {
        Review expected = reviewWithoutId();
        String expectedJson = toJson(expected);
        when(movies.existsById(Mockito.any())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReviewNotFound() throws Exception {
        Review updated = reviewWithId();
        String updatedJson = toJson(updated);
        when(reviews.findById(Mockito.any())).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put("/reviews/" + updated.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReview() throws Exception {
        Movie movie = movieWithId();
        when(movies.findById(movie.getId())).thenReturn(Optional.of(movie));

        Review existing = reviewWithId();
        Review updated = reviewWithId();
        updated.setMovieId(movie.getId());
        updated.setTitle("New Review Title!");
        updated.setBody("New Review Body");
        updated.setRating(10);
        String updatedJson = toJson(updated);

        when(reviews.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(reviews.save(Mockito.any())).thenReturn(updated);

        mvc.perform(MockMvcRequestBuilders.put("/reviews/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updated.getTitle()))
                .andExpect(jsonPath("$.body").value(updated.getBody()))
                .andExpect(jsonPath("$.rating").value(updated.getRating()));
    }

    @Test
    void deleteReview() throws Exception {
        when(reviews.existsById(1234L)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/reviews/1234"))
                .andExpect(status().isOk());
    }

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}