package com.daugherty.movie.collector.repository;

import com.daugherty.movie.collector.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Movies extends JpaRepository<Movie, Long> {
}
