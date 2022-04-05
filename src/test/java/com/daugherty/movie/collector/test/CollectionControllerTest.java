package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.controller.CollectionController;
import com.daugherty.movie.collector.controller.MovieNotFoundException;
import com.daugherty.movie.collector.controller.ReviewNotFoundException;
import com.daugherty.movie.collector.dto.DtoConverter;
import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.themoviedb.api.MovieDbService;
import org.themoviedb.api.dto.TmdbMovie;

import java.util.List;
import java.util.Optional;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(CollectionController.class)
class CollectionControllerTest {

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

    @Test
    void getAllMoviesWithNoMovies() {
        assertTrue(controller.getAllMovies().getContent().isEmpty());
    }

    @Test
    void getAllMovies() {
        List<Movie> expected = List.of(movieWithId());
        given(movies.findAll()).willReturn(expected);

        CollectionModel<MovieDto> allMovies = controller.getAllMovies();

        assertFalse(allMovies.getLinks("self").isEmpty());
        allMovies.getContent().forEach(m -> assertFalse(m.getLinks("self").isEmpty()));

        MovieDto result = allMovies.getContent().iterator().next();
        assertEquals(expected.get(0).getId(), result.getId());
        assertEquals(expected.get(0).getTitle(), result.getTitle());
        assertEquals(expected.get(0).getTmdbId(), result.getTmdbId());
    }

    @Test
    void getMovieById() {
        Movie expected = movieWithId();
        given(movies.findById(expected.getId())).willReturn(Optional.of(expected));

        MovieDto result = controller.getMovieById(expected.getId());

        assertFalse(result.getLinks("self").isEmpty());
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getTmdbId(), result.getTmdbId());
    }

    @Test
    void getMovieDetails() {
        TmdbMovie tmdbMovie = tmdbMovie();

        Movie movie = movieWithId();
        movie.setTmdbId(tmdbMovie.getId());
        movie.setTitle(tmdbMovie.getTitle());

        Review review = reviewWithId();
        review.setMovieId(movie.getId());

        given(movies.findById(movie.getId())).willReturn(Optional.of(movie));
        given(reviews.findByMovieId(movie.getId())).willReturn(List.of(review));
        given(movieDbService.getMovieById(movie.getTmdbId())).willReturn(Optional.of(tmdbMovie));

        MovieDetailsDto dto = controller.getMovieDetails(movie.getId());

        // verify movie included
        assertEquals(movie.getId(), dto.getId());
        assertEquals(movie.getTitle(), dto.getTitle());

        // verify TMDB info included
        assertEquals(tmdbMovie.getTagline(), dto.getTagline());
        assertEquals(tmdbMovie.getTagline(), dto.getTagline());

        // verify review included
        assertEquals(1, dto.getReviews().size());
        assertEquals(review.getTitle(), dto.getReviews().get(0).getTitle());
    }

    @Test
    void getMovieByIdNotFound() {
        Assertions.assertThrows(MovieNotFoundException.class,
                () -> controller.getMovieById(Long.MIN_VALUE));
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
                .andExpect(jsonPath("$.title").value(expected.getTitle()))
                .andExpect(jsonPath("_links.self.href").exists());
    }

    @Test
    void deleteMovie() throws Exception {
        given(movies.existsById(1234L)).willReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/movies/1234"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllReviewsWithNoReviews() {
        assertTrue(controller.getAllReviews().getContent().isEmpty());
    }

    @Test
    void getAllReviews() {
        List<Review> expected = List.of(reviewWithId());
        given(reviews.findAll()).willReturn(expected);

        CollectionModel<ReviewDto> allReviews = controller.getAllReviews();

        assertFalse(allReviews.getLinks("self").isEmpty());
        allReviews.getContent().forEach(r -> assertFalse(r.getLinks("self").isEmpty()));

        ReviewDto result = allReviews.getContent().iterator().next();
        assertEquals(expected.get(0).getTitle(), result.getTitle());
    }

    @Test
    void findReviewsByMovieId() {
        List<Review> expected = List.of(reviewWithId());
        given(reviews.findByMovieId(2)).willReturn(expected);

        CollectionModel<ReviewDto> allReviews = controller.findReviewsByMovieId(2);

        assertFalse(allReviews.getLinks("self").isEmpty());
        allReviews.getContent().forEach(r -> assertFalse(r.getLinks("self").isEmpty()));

        ReviewDto result = allReviews.getContent().iterator().next();
        assertEquals(expected.get(0).getTitle(), result.getTitle());
    }

    @Test
    void getReviewByIdNotFound() {
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> controller.getReviewById(Long.MIN_VALUE));
    }

    @Test
    void getReviewById() {
        Review expected = reviewWithId();
        given(reviews.findById(expected.getId())).willReturn(Optional.of(expected));

        ReviewDto result = controller.getReviewById(expected.getId());

        assertFalse(result.getLinks("self").isEmpty());

        assertEquals(expected.getTitle(), result.getTitle());
    }

    @Test
    void addNewReview() throws Exception {
        Movie movie = movieWithId();
        given(movies.existsById(movie.getId())).willReturn(true);

        Review expected = reviewWithoutId();
        expected.setMovieId(movie.getId());
        String expectedJson = toJson(new DtoConverter().toDto(expected));
        given(reviews.save(Mockito.any())).willReturn(expected);

        mvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(expected.getTitle()))
                .andExpect(jsonPath("_links.self.href").exists());
    }

    @Test
    void addNewReviewMovieNotFound() throws Exception {
        Review expected = reviewWithoutId();
        String expectedJson = toJson(expected);
        given(movies.existsById(Mockito.any())).willReturn(false);

        mvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReviewNotFound() throws Exception {
        Review updated = reviewWithId();
        String updatedJson = toJson(updated);
        given(reviews.findById(Mockito.any())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put("/reviews/" + updated.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReview() throws Exception {
        Movie movie = movieWithId();
        given(movies.findById(movie.getId())).willReturn(Optional.of(movie));

        Review existing = reviewWithId();
        Review updated = reviewWithId();
        updated.setMovieId(movie.getId());
        updated.setTitle("New Review Title!");
        updated.setBody("New Review Body");
        updated.setRating(10);
        String updatedJson = toJson(updated);

        given(reviews.findById(existing.getId())).willReturn(Optional.of(existing));
        given(reviews.save(Mockito.any())).willReturn(updated);

        mvc.perform(MockMvcRequestBuilders.put("/reviews/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updated.getTitle()))
                .andExpect(jsonPath("$.body").value(updated.getBody()))
                .andExpect(jsonPath("$.rating").value(updated.getRating()))
                .andExpect(jsonPath("_links.self.href").exists());
    }

    @Test
    void deleteReview() throws Exception {
        given(reviews.existsById(1234L)).willReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/reviews/1234"))
                .andExpect(status().isOk());
    }

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}