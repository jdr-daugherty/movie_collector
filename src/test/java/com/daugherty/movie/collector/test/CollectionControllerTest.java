package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.controller.CollectionController;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(CollectionController.class)
class CollectionControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private Movies movies;

    @MockBean
    private Reviews reviews;

    @Autowired
    private CollectionController controller;

    @Test
    void getAllMoviesWithNoMovies() {
        assertTrue(controller.getAllMovies().isEmpty());
    }

    @Test
    void getAllMovies() {
        List<Movie> expected = List.of(movieWithId());
        given(movies.findAll()).willReturn(expected);

        assertEquals(expected, controller.getAllMovies());
    }

    @Test
    void getMovieById() {
        Movie expected = movieWithId();
        given(movies.findById(expected.getId())).willReturn(Optional.of(expected));

        ResponseEntity<Movie> result = controller.getMovieById(expected.getId());
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(expected, result.getBody());
    }

    @Test
    void getMovieByIdNotFound() {
        ResponseEntity<Movie> result = controller.getMovieById(Long.MIN_VALUE);
        assertTrue(result.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    void addNewMovie() throws Exception {
        Movie expected = movieWithoutId();
        String expectedJson = toJson(expected);
        given(movies.save(Mockito.any())).willReturn(expected);

        mvc.perform(MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));
    }

    @Test
    void addNewReview() throws Exception {
        Review expected = reviewWithoutId();
        String expectedJson = toJson(expected);
        given(reviews.save(Mockito.any())).willReturn(expected);

        mvc.perform(MockMvcRequestBuilders.post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));
    }

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Test
    void getAllReviewsWithNoReviews() {
        assertTrue(controller.getAllReviews().isEmpty());
    }

    @Test
    void getAllReviews() {
        List<Review> expected = List.of(reviewWithId());
        given(reviews.findAll()).willReturn(expected);

        assertEquals(expected, controller.getAllReviews());
    }

    @Test
    void getReviewByIdNotFound() {
        ResponseEntity<Review> result = controller.getReviewById(Long.MIN_VALUE);
        assertTrue(result.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    void getReviewById() {
        Review expected = reviewWithId();
        given(reviews.findById(expected.getId())).willReturn(Optional.of(expected));

        ResponseEntity<Review> result = controller.getReviewById(expected.getId());
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(expected, result.getBody());
    }
}