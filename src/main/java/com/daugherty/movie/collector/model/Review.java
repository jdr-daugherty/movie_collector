package com.daugherty.movie.collector.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String title;

    private String body;

    private long movieId;

    @NotNull
    private Date reviewed;

    @Max(10)
    @Min(0)
    private Integer rating;

    public Review(String title, long movieId) {
        this.title = title;
        this.movieId = movieId;
        this.reviewed = new Date();
        this.rating = 5;
    }
}
