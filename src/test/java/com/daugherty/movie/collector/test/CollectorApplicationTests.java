package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.controller.MoviesController;
import com.daugherty.movie.collector.repository.Movies;
import com.daugherty.movie.collector.repository.Reviews;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
class CollectorApplicationTests {

    @Autowired
    private Movies movies;

    @Autowired
    private Reviews reviews;

    @Autowired
    private MoviesController controller;

    @Test
    void contextLoads() {
        assertNotNull(movies);
        assertNotNull(reviews);
        assertNotNull(controller);
    }
}
