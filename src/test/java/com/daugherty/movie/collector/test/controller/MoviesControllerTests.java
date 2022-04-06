package com.daugherty.movie.collector.test.controller;

import com.daugherty.movie.collector.controller.MoviesController;
import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.service.MoviesService;
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

import java.util.List;

import static com.daugherty.movie.collector.test.TestObjectMother.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(MoviesController.class)
class MoviesControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MoviesService service;

    @Test
    void getAllMovies() throws Exception {
        final List<MovieDto> expected = twoMovieDtosWithIds();

        when(service.getAllMovies()).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get("/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(expected.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(expected.get(1).getTitle()));

        verify(service, times(1)).getAllMovies();
    }

    @Test
    void getMovieById() throws Exception {
        MovieDto expected = movieDtoWithId();
        when(service.getMovieById(expected.getId())).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get("/movies/"+expected.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));

        verify(service, times(1)).getMovieById(expected.getId());
    }

    @Test
    void getMovieDetails() throws Exception {
        MovieDetailsDto expected = movieDetailsDto();
        when(service.getMovieDetails(expected.getId())).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get("/movies/"+expected.getId()+"/details")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));

        verify(service, times(1)).getMovieDetails(expected.getId());
    }

    @Test
    void getMovieByIdNotFound() throws Exception {
        when(service.getMovieById(anyLong())).thenThrow(new MovieNotFoundException());

        mvc.perform(MockMvcRequestBuilders.get("/movies/1234567890")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getMovieById(anyLong());
    }

    @Test
    void addNewMovie() throws Exception {
        MovieDto expected = movieDtoWithoutId();
        String expectedJson = toJson(expected);
        when(service.addNewMovie(Mockito.any())).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(expected.getTitle()));
    }

    @Test
    void deleteMovie() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/movies/1234"))
                .andExpect(status().isOk());
    }

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}