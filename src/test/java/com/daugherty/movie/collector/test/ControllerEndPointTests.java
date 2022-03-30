package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.controller.CollectionController;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static com.daugherty.movie.collector.test.TestObjectMother.listOfMovies;
import static com.daugherty.movie.collector.test.TestObjectMother.listOfReviews;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(CollectionController.class)
public class ControllerEndPointTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CollectionController controller;

    @Test
    public void getAllMovies() throws Exception {
        given(controller.getAllMovies()).willReturn(listOfMovies());

        mvc.perform(get("/movies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getMovieById() throws Exception {
        Movie expected = TestObjectMother.movieWithId();
        given(controller.getMovieById(expected.getId())).willReturn(ResponseEntity.ok(expected));

        mvc.perform(get("/movies/" + expected.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Reservoir Dogs")));
    }

    @Test
    public void getAllReviews() throws Exception {
        given(controller.getAllReviews()).willReturn(listOfReviews());

        mvc.perform(get("/reviews").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getReviewById() throws Exception {
        Review expected = TestObjectMother.reviewWithId();
        given(controller.getReviewById(expected.getId())).willReturn(ResponseEntity.ok(expected));

        mvc.perform(get("/reviews/" + expected.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Reservoir Dogs")));
    }
}
