package com.daugherty.movie.collector.repository;

import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Reviews extends JpaRepository<Review, Long> {
}
