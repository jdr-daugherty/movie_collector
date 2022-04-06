package com.daugherty.movie.collector.details.themoviedb.dto;

import com.daugherty.movie.collector.details.MovieDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TmdbMovie implements MovieDetails {
    private long id;
    private String title;
    private String tagline;
    private String overview;
    private boolean adult;

    @JsonProperty("release_date")
    private Date releaseDate;

    @JsonProperty("vote_average")
    private double voteAverage;

    private int runtime;
}
