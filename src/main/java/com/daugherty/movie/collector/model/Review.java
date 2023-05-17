package com.daugherty.movie.collector.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", length = 20480)
    private String body;

    @Column(name = "movie_id", nullable = false)
    private long movieId;

    @Column(name = "reviewed", nullable = false)
    private Date reviewed;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    public Review(String title, long movieId) {
        this.title = title;
        this.movieId = movieId;
        this.reviewed = new Date();
        this.rating = 5;
    }
}
