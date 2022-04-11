package com.daugherty.movie.collector.details.themoviedb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieList {
    @JsonProperty("results")
    private List<TmdbMovie> results;
}
