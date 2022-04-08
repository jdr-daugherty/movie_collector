package com.daugherty.movie.collector.test.controller;

import com.daugherty.movie.collector.controller.ReviewsController;
import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.exception.ReviewNotFoundException;
import com.daugherty.movie.collector.service.ReviewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static java.lang.String.format;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(ReviewsController.class)
class ReviewsControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReviewsService service;

    @Test
    void getAllReviews() throws Exception {
        final List<ReviewDto> expected = twoReviewsWithIds();

        when(service.getAllReviews()).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get("/reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(expected.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(expected.get(1).getTitle()));

        verify(service, times(1)).getAllReviews();
    }

    @Test
    void findReviewsByMovieId() throws Exception {
        final List<ReviewDto> expected = twoReviewsWithIds();
        final long movieId = expected.get(0).getMovieId();

        when(service.findReviewsByMovieId(movieId)).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(format("/reviews/by_movie/%d", movieId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(expected.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(expected.get(1).getTitle()));

        verify(service, times(1)).findReviewsByMovieId(movieId);
    }

    @Test
    void getReviewByIdNotFound() throws Exception {
        final long missingId = 1234567890L;

        when(service.getReviewById(missingId))
                .thenThrow(new ReviewNotFoundException());

        mvc.perform(MockMvcRequestBuilders.get("/reviews/"+missingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getReviewById(missingId);
    }

    @Test
    void getReviewById() throws Exception {
        ReviewDto expected = reviewDtoWithId();
        when(service.getReviewById(expected.getId())).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get("/reviews/" + expected.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));

        verify(service, times(1)).getReviewById(expected.getId());
    }

    @Test
    void addNewReview() throws Exception {
        when(service.addNewReview(any(ReviewDto.class)))
                .then(returnsFirstArg());

        ReviewDto expected = reviewDtoWithoutId();

        mvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(expected)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));

        verify(service, times(1)).addNewReview(any(ReviewDto.class));
    }

    @Test
    void addNewReviewMovieNotFound() throws Exception {
        when(service.addNewReview(any(ReviewDto.class)))
                .thenThrow(new MovieNotFoundException());

        mvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(reviewDtoWithoutId())))
                .andExpect(status().isNotFound());

        verify(service, times(1)).addNewReview(any(ReviewDto.class));
    }

    @Test
    void updateReviewNotFound() throws Exception {
        ReviewDto updated = reviewDtoWithId();
        String updatedJson = toJson(updated);
        when(service.updateReview(any(ReviewDto.class), anyLong()))
                .thenThrow(new ReviewNotFoundException());

        mvc.perform(MockMvcRequestBuilders.put("/reviews/" + updated.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isNotFound());

        verify(service, times(1)).updateReview(any(ReviewDto.class), anyLong());
    }

    @Test
    void updateReview() throws Exception {
        when(service.updateReview(any(ReviewDto.class), anyLong()))
                .then(returnsFirstArg());

        ReviewDto updated = reviewDtoWithId();
        updated.setTitle("New Review Title!");
        updated.setBody("New Review Body");
        updated.setRating(10);

        mvc.perform(MockMvcRequestBuilders.put("/reviews/" + updated.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updated.getTitle()))
                .andExpect(jsonPath("$.body").value(updated.getBody()))
                .andExpect(jsonPath("$.rating").value(updated.getRating()));

        verify(service, times(1)).updateReview(any(ReviewDto.class), anyLong());
    }

    @Test
    void deleteReview() throws Exception {
        doNothing().when(service).deleteReview(anyLong());

        mvc.perform(MockMvcRequestBuilders.delete("/reviews/1234"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteReview(anyLong());
    }

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}